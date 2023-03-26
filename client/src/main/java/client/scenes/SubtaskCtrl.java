package client.scenes;

import client.utils.ServerUtils;
import commons.Subtask;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
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

    private Subtask subtask;
    public void setServerAndCtrl(ServerUtils server, MainCtrl mainCtrl){
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

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
        server.updateSubtaskTitle(subtask.id, editableTitle.getText());
    }
}
