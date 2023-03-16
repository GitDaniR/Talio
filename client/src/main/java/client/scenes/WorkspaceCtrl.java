package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javax.inject.Inject;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class WorkspaceCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private User user;

    private ObservableList<Board> data;
    @FXML
    private Button createBoard;

    @FXML
    private Button joinBoard;
    @FXML
    private AnchorPane preview;
    @Inject
    public WorkspaceCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void createBoard(){
        mainCtrl.showNewBoard(this.user);
    }

    public void setUser(String username){
            this.user = server.getUserByUsername(username);
            if(this.user == null)
                this.user = server.addUser(new User(username));

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private BoardDummy setCtrl(FXMLLoader boardLoader, Board board){
        BoardDummy ctrl = boardLoader.getController();
        ctrl.setServerAndCtrl(server, mainCtrl);
        ctrl.setBoard(board);
        return ctrl;

    }
    private void addBoardToJoined(Node boardObject){
        // vbox with all joined boards is at index 2
        ((VBox)preview.getChildren().get(2)).getChildren().add(boardObject);
    }

    public void refresh(){
        var user = server.getUserByUsername(this.user.username);
        List<Board> boards = user.boardsJoinedByUser;
        data = FXCollections.observableList(boards);

        for(Board board: boards){
            FXMLLoader boardDummyLoader = new FXMLLoader(getClass().
                    getResource("boardWorkspaceDummy.fxml"));
            try {
                Node boardObject = boardDummyLoader.load();
                BoardDummy boardCtrl = setCtrl(boardDummyLoader, board);
                addBoardToJoined(boardObject);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
