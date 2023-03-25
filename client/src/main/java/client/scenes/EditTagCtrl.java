package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Tag;
import jakarta.ws.rs.WebApplicationException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class EditTagCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private TextField title;
    @FXML
    private ColorPicker color;
    @FXML
    private Label oldTitle;
    @FXML
    private Label oldColor;

    @FXML
    private Button save;
    @FXML
    private Button cancel;
    private Tag tagToEdit;

    @Inject
    public EditTagCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void cancel(){
        clearFields();
        mainCtrl.showBoard();
    }

    private void clearFields() {
        title.clear();
    }

    public void ok() {
        try {
            server.editTagTitle(tagToEdit.id, title.getText());
            server.editTagColor(tagToEdit.id, color.getValue().toString());

        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        clearFields();
        mainCtrl.showTagOverview(tagToEdit.board);
    }

    public void setTagToEdit(Tag tagToEdit) {
        this.tagToEdit = tagToEdit;
    }

    public void refresh(){
        int id = tagToEdit.id;
        try{
            //fetch the tag from the server
            tagToEdit = server.getTagById(id);
            oldTitle.setText(tagToEdit.title);
            oldColor.setText(tagToEdit.color);
        }catch(Exception e){
            //if it doesn't exist someone probably deleted it while we were editing the tag
            //so we are returned to the board overview
            e.printStackTrace();
            mainCtrl.showBoard();
        }
    }
}
