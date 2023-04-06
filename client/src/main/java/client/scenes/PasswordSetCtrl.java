package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import com.google.inject.Inject;
import javafx.stage.Modality;

import java.net.URL;
import java.util.ResourceBundle;

public class PasswordSetCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Board boardToEdit;
    @FXML
    private TextField newPassword;

    @Inject
    public PasswordSetCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }
    public void setBoard(Board board){
        this.boardToEdit = board;
    }
    private void clearFields() {
        newPassword.clear();
    }
    public void cancel() {
        clearFields();
        mainCtrl.showBoard();
    }

    public void save() {
        try{
            if(newPassword.getText().equals(""))
                server.updateBoardPassword(boardToEdit.id,"NO_PASSWORD");
            else{
                server.updateBoardPassword(boardToEdit.id, newPassword.getText());
            }
        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        clearFields();
        mainCtrl.showBoard();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainCtrl.consumeShortcutsTextField(newPassword);
    }
}
