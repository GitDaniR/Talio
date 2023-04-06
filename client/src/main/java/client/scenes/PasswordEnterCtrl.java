package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.User;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import com.google.inject.Inject;
import javafx.stage.Modality;

import java.net.URL;
import java.util.ResourceBundle;

public class PasswordEnterCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Board boardToEdit;
    private User viewingUser;
    @FXML
    private TextField newPassword;

    @Inject
    public PasswordEnterCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }
    public void setBoardAndUser(User viewingUser, Board board){
        this.viewingUser = viewingUser;
        this.boardToEdit = board;
    }
    private void clearFields() {
        newPassword.clear();
    }
    public void cancel() {
        clearFields();
        mainCtrl.showBoard();
    }

    public void unlock() {
        try{
            if(newPassword.getText().equals(boardToEdit.password)){
                server.assignBoardToUserWrite(viewingUser.id,boardToEdit.id);
                clearFields();
                mainCtrl.showBoard();
            } else
                throw new WebApplicationException("Wrong password!");
        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainCtrl.consumeShortcutsTextField(newPassword);
    }
}
