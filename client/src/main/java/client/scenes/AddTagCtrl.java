package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Tag;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import java.net.URL;
import java.util.ResourceBundle;

public class AddTagCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private TextField title;
    @FXML
    private ColorPicker colorPicker;

    private Board boardToAddTo;

    @Inject
    public AddTagCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void cancel(){
        clearFields();
        mainCtrl.showTagOverview(this.boardToAddTo);
    }

    private void clearFields() {
        title.clear();
        colorPicker.setValue(Color.valueOf("0xffffff"));
    }

    public void ok() {
        try {
            // does not allow a blank title
            if(title.getText().isBlank()){
                var alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText("Title cannot be blank");
                alert.showAndWait();
                return;
            }
            server.addTag(getTag());

        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        clearFields();
        mainCtrl.showTagOverview(boardToAddTo);
    }

    private Tag getTag() {
        var t = title.getText();
        var c = colorPicker.getValue().toString();

        return new Tag(t, c, boardToAddTo, boardToAddTo.id);
    }

    public void setBoard(Board board){
        this.boardToAddTo = board;
    }
}
