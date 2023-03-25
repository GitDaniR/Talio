package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
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

public class AddTagCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private TextField title;
    @FXML
    private ColorPicker color;

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
    }

    public void ok() {
        try {
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
        var c = color.getValue().toString();

        return new Tag(t, c, boardToAddTo, boardToAddTo.id);
    }

    public void setBoard(Board board){
        this.boardToAddTo = board;
    }
}
