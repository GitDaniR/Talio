package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javax.inject.Inject;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
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
    private Button disconnectButton;
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

    /**
     * Method that sets the user for the workspace
     * @param username - username of the user
     */
    public void setUser(String username){
        this.user = server.getUserByUsername(username);
        if(this.user == null)this.user = server.addUser(new User(username));

    }

    /**
     * Method that disconnects the user and redirect to
     * welcome page
     */
    public void disconnect(){
        mainCtrl.showWelcomePage();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Method that sets controller for the javaFX object
     * that represent joined board
     * @param boardLoader
     * @param board
     * @return
     */
    private BoardDummy setCtrl(FXMLLoader boardLoader, Board board){
        BoardDummy ctrl = boardLoader.getController();
        ctrl.setServerAndCtrl(server, mainCtrl);
        ctrl.setBoard(board);
        return ctrl;

    }

    /**
     * Method that adds Board to the list of joined boards
     * in the preview
     * @param boardObject - board scene to be added
     * @param indexOfBoard - index of the board in the data
     */
    private void addBoardToJoined(Node boardObject, int indexOfBoard){
        // vbox with all joined boards is at index 3
        ((VBox)preview.getChildren().get(3)).getChildren().add(boardObject);
        HBox boardVbox = (HBox) boardObject;
        int userId = this.user.id;
        ((Button)(boardVbox.getChildren().get(1))).setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                server.removeBoardFromJoined(userId, data.get(indexOfBoard).id);
                refresh();
            }
        });
    }

    /**
     * Method that clears the preview and removes all boards
     * from the preview
     */
    private void clearJoinedBoards(){
        ((VBox)preview.getChildren().get(3)).getChildren().clear();
    }

    /**
     * Method that refreshes the preview
     */
    public void refresh(){

        this.user = server.getUserByUsername(this.user.username);
        List<Board> boards = user.boards;
        data = FXCollections.observableList(boards);
        clearJoinedBoards();
        for(Board board: boards){
            FXMLLoader boardDummyLoader = new FXMLLoader(getClass().
                    getResource("boardWorkspaceDummy.fxml"));
            try {
                Node boardObject = boardDummyLoader.load();
                BoardDummy boardCtrl = setCtrl(boardDummyLoader, board);
                addBoardToJoined(boardObject, boards.indexOf(board));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
