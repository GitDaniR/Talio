package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import jakarta.ws.rs.WebApplicationException;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import com.google.inject.Inject;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class ChangeBoardTitleCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Board boardToEdit;
    @FXML
    private TextField newTitle;
    @FXML
    private Label oldTitle;
    @FXML
    private Label errorLabel;

    @Inject
    public ChangeBoardTitleCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }
    public void setBoard(Board board){
        this.boardToEdit = board;
        oldTitle.setText(board.title);
    }
    private void clearFields() {
        newTitle.clear();
    }
    public void cancel() {
        clearFields();
        mainCtrl.showBoard();
    }
    private void displayError(){
        errorLabel.setText("Title cannot be empty!");
        // message gets deleted after 2 seconds
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> {
            errorLabel.setText("");
        });
        delay.play();
    }

    public void editBoard() {
        if(newTitle.getText().length()==0){
            displayError();
            return;
        }
        try{
            server.updateBoardTitle(boardToEdit.id, newTitle.getText());
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
        mainCtrl.consumeShortcutsTextField(newTitle);
    }
}
