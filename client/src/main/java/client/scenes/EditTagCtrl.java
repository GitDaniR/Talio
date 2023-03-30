package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Tag;
import jakarta.ws.rs.WebApplicationException;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class EditTagCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private TextField title;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private Label oldTitle;
    @FXML
    private Label oldColor;
    @FXML
    private Label errorLabel;
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
        mainCtrl.showTagOverview(server.getBoardByID(tagToEdit.boardId));
    }

    private void clearFields() {
        title.clear();
        colorPicker.setValue(Color.valueOf("0xffffff"));
        oldTitle.setText("");
        oldColor.setText("");
    }

    private PauseTransition delay;
    public void displayErrorText(String text){
        errorLabel.setText(text);
        // message gets deleted after 2 seconds
        if(delay!=null)
            delay.stop();//stop previous delay
        delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> {
            errorLabel.setText("");
        });
        delay.play();
    }

    public void ok() {
        if(title.getText().equals("")){
            displayErrorText("Tag title cannot be empty!");
            return;
        }
        try {
            server.editTagTitle(tagToEdit.id, title.getText());
            server.editTagColor(tagToEdit.id, colorPicker.getValue().toString());
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        clearFields();
        mainCtrl.showTagOverview(server.getBoardByID(tagToEdit.boardId));
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
