package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class BoardWorkspaceCtrl implements Initializable {

    @FXML
    private Label lblBoardName;

    @FXML
    private Button btnOpen;

    @FXML
    private Button btnClose;

    private MainCtrl mainCtrl;
    private ServerUtils server;
    private Board board;

    public void setMainCtrlAndServer(MainCtrl mainCtrl, ServerUtils server){
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void setBoard(Board board){
        this.board = board;
        lblBoardName.setText(board.title);
    }

    public void openBoard(){
        //TODO make this actually open the board
        System.out.println("Open board " + board.id);
    }

    public void leaveBoard(){
        //TODO make this actually leave the board
        System.out.println("Leave board " + board.id);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
