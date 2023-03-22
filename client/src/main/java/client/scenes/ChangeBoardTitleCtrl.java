package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import jakarta.ws.rs.WebApplicationException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import com.google.inject.Inject;

import java.util.Timer;
import java.util.TimerTask;

public class ChangeBoardTitleCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Board boardToEdit;
    @FXML
    private TextField newTitle;
    @FXML
    private Label oldTitle;

    @Inject
    public ChangeBoardTitleCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }
    public void setBoard(Board board){
        this.boardToEdit = board;
    }
    private void clearFields() {
        newTitle.clear();
    }
    public void cancel() {
        clearFields();
        mainCtrl.showBoard();
    }

    public void editBoard() {
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
        int id = boardToEdit.id;
        try{
            //fetch the board from the server
            boardToEdit = server.getBoardByID(id);
            oldTitle.setText(boardToEdit.title);
        }catch(Exception e){
            e.printStackTrace();
            mainCtrl.showBoard();
        }
    }
}
