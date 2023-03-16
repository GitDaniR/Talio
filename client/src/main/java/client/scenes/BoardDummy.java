package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class BoardDummy implements Initializable {

    @FXML
    private TextField boardTitle;
    @FXML
    private Button openButton;
    @FXML
    private Button leaveButton;


    private Board board;

    private ServerUtils server;
    private MainCtrl mainCtrl;

    public void setServerAndCtrl(ServerUtils server,MainCtrl mainCtrl){
        this.server=server;
        this.mainCtrl=mainCtrl;
    }

    public void setBoard(Board board){
        this.board = board;
        this.boardTitle.setText(board.title);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
