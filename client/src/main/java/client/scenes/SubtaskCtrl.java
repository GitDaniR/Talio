package client.scenes;

import client.utils.ServerUtils;
import commons.Card;
import commons.Subtask;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;

public class SubtaskCtrl implements Initializable {

    private  ServerUtils server;
    private  MainCtrl mainCtrl;

    @FXML
    private Text title;

    @FXML
    private CheckBox check;

    @FXML
    Button remove;

    @FXML
    private TextField editableTitle;

    @FXML
    private Button moveUp;

    @FXML
    private Button moveDown;

    private Subtask subtask;
    public void setServerAndCtrl(ServerUtils server, MainCtrl mainCtrl){
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        editableTitle.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Text changed from " + oldValue + " to " + newValue);
            server.updateSubtaskTitle(subtask.id, newValue);
        });
        editableTitle.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                // Text field lost focus
                System.out.println("Text field lost focus");
                editableTitle.setVisible(false);
            }
        });
    }

    public void finishTask(){
        server.updateSubtaskStatus(subtask.id, true);
    }

    public void unfinishTask(){
        server.updateSubtaskStatus(subtask.id, false);
    }

    public void removeTask(){
        server.removeSubtask(subtask.id);
    }
    public void setSubtask(Subtask subtask){
        this.subtask = subtask;
    }

    public Text getTitle() {
        return this.title;
    }

    public TextField getEditableTitle() {
        return this.editableTitle;
    }

    @FXML
    public void onEnter(ActionEvent ae) {
        editableTitle.setVisible(false);
    }

    public void subtaskMoveUp() {
        int currentIndex = subtask.index;
        if (currentIndex >= 1) {
            Card c = server.getCardById(subtask.cardId);
            Collections.sort(c.subtasks, Comparator.comparingInt(s -> s.index));
            Subtask subtaskAbove = c.subtasks.get(currentIndex - 1);
            server.updateSubtaskIndex(subtask.id, currentIndex - 1);
            server.updateSubtaskIndex(subtaskAbove.id, currentIndex);
        }
    }

    public void subtaskMoveDown() {
        int currentIndex = subtask.index;
        Card c = server.getCardById(subtask.cardId);
        int maxIndex = c.subtasks.size() - 1;
        if (currentIndex < maxIndex) {
            Collections.sort(c.subtasks, Comparator.comparingInt(s -> s.index));
            Subtask subtaskBelow = c.subtasks.get(currentIndex + 1);
            server.updateSubtaskIndex(subtask.id, currentIndex + 1);
            server.updateSubtaskIndex(subtaskBelow.id, currentIndex);
        }
    }
}
