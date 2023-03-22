package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class BoardWorkspaceAdminCtrl implements Initializable {

    @FXML
    private Label lblBoardName;

    @FXML
    private Button btnOpen;

    @FXML
    private Button btnDelete;

    private MainCtrl mainCtrl;
    private ServerUtils server;

    private WorkspaceAdminCtrl workspaceAdminCtrl;
    private Board board;

    private User user;

    public void setMainCtrlAndServer(MainCtrl mainCtrl, ServerUtils server,
                                     WorkspaceAdminCtrl workspaceAdminCtrl){
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.workspaceAdminCtrl = workspaceAdminCtrl;
    }

    public void setBoard(Board board){
        this.board = board;
        lblBoardName.setText(board.title);
    }

    public void setUser(User user){
        this.user = user;
    }
    public void openBoard(){
        mainCtrl.showBoard(board);
    }

    public void deleteBoard(){
        server.deleteBoard(board.id);
        this.workspaceAdminCtrl.refresh();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
