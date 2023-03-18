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
import javafx.scene.control.TextField;
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
    private VBox boardsDisplay;
    @FXML private TextField txtBoardName;

    @Inject
    public WorkspaceCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void createBoard(){
        //TODO pass the title to the new board
        mainCtrl.showNewBoard(this.user);
    }

    public void joinBoard(){
        //TODO make a call to the server that the board was joined
        // and then switch to its scene
        System.out.println("Joining board " + txtBoardName.getText());
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
    private BoardWorkspaceCtrl setCtrl(FXMLLoader boardLoader, Board board){
        BoardWorkspaceCtrl ctrl = boardLoader.getController();
        ctrl.setMainCtrlAndServer(mainCtrl, server);
        ctrl.setBoard(board);
        return ctrl;
    }

    /**
     * Method that clears the preview and removes all boards
     * from the preview
     */
    private void clearJoinedBoards(){
        boardsDisplay.getChildren().clear();
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
            FXMLLoader boardWorkspaceLoader = new FXMLLoader(getClass().
                    getResource("BoardWorkspace.fxml"));
            try {
                Node boardObject = boardWorkspaceLoader.load();
                BoardWorkspaceCtrl boardCtrl = setCtrl(boardWorkspaceLoader, board);
                boardsDisplay.getChildren().add(boardObject);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
