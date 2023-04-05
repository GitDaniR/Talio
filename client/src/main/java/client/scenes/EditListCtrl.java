package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.BoardList;
import jakarta.ws.rs.WebApplicationException;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class EditListCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private BoardList boardListToEdit;

    @FXML
    private TextField newTitle;
    @FXML
    private Label oldTitle;
    @FXML
    private Label errorLabel;

    @Inject
    public EditListCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void setBoardListToEdit(BoardList boardListToEdit){
        this.boardListToEdit=boardListToEdit;
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

    public void editList() {
        if(newTitle.getText().equals("")){
            displayErrorText("List title cannot be empty!");
            return;
        }
        try {
            server.updateBoardListTitle(boardListToEdit.id, newTitle.getText());
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
    private void clearFields() {
        newTitle.clear();
    }

    public void cancel() {
        clearFields();
        mainCtrl.showBoard();
    }

    public Timer startTimer(int refreshRate){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(()->{
                    refresh();
                });
            }
        }, 0, refreshRate);
        return timer;
    }

    public void refresh(){
        int id = boardListToEdit.id;
        try{
            //fetch the board list from the server
            boardListToEdit = server.getBoardListById(id);
            oldTitle.setText(boardListToEdit.title);
        }catch(Exception e){
            //if it doesn't exist someone probably deleted it while we were adding a card
            //so we are returned to the board overview
            e.printStackTrace();
            mainCtrl.showBoard();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainCtrl.consumeShortcutsTextField(newTitle);
    }
}
