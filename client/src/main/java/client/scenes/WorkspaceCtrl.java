package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.User;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import javax.inject.Inject;
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
    private TextField inputBoardToJoin;
    @FXML
    private Button disconnectButton;
    @FXML
    private VBox boardsDisplay;
    @FXML
    private TextField txtBoardName;
    @FXML
    private Label alreadyJoinedText;

    @Inject
    public WorkspaceCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        server.registerForMessages("/topic/boards/removed", Integer.class, boardId -> {
            Platform.runLater(() -> removeBoard(boardId));
        });
        server.registerForMessages("/topic/boards/rename", Board.class, newBoard -> {
            Platform.runLater(() -> { renameBoard(newBoard.id,newBoard.title); });
        });
    }

    /**
     * Method that sets the user for the workspace
     * @param username - username of the user
     */
    public void setUser(String username){
        this.user = server.getUserByUsername(username);
        if(this.user == null)this.user = server.addUser(new User(username));
    }

    //region SOCKET METHODS

    private void renameBoard(int id, String title){
        boardsDisplay.getChildren().stream()
                .filter(e -> ((BoardWorkspaceCtrl)e.getUserData()).getBoard().id==id)
                .findFirst()
                .ifPresent(e -> ((BoardWorkspaceCtrl) e.getUserData())
                        .getLblBoardName().setText(title));
    }

    private void removeBoard(int boardId){
        if(user.hasBoardAlready(boardId))
            boardsDisplay.getChildren().
                    removeIf(e ->
                    ((BoardWorkspaceCtrl)e.getUserData()).getBoard().id==boardId);
    }

    //endregion

    //region Button methods

    public void createBoard(){
        mainCtrl.showNewBoard(this.user);
    }

    private PauseTransition delay;

    public void displayText(String text){
        alreadyJoinedText.setText(text);
        // message gets deleted after 2 seconds
        if(delay!=null)
            delay.stop();//stop previous delay
        delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> {
            alreadyJoinedText.setText("");
        });
        delay.play();
    }

    public void clearInviteText(){
        inputBoardToJoin.clear();
    }

    public void joinInputBoard() throws Exception {
        // Take the ID out of inputBoardToJoin (integer after "#")
        String[] boardToJoin = inputBoardToJoin.getText().split("#");
        int boardToJoinId;

        if(inputBoardToJoin.getText().length()==0){
            return;
        }

        try {
            boardToJoinId = Integer.parseInt(boardToJoin[boardToJoin.length-1]);
            Board chosenBoard = server.getBoardByID(boardToJoinId); // Take the board with that ID
            if(!user.hasBoardAlready(chosenBoard.id))
                mainCtrl.joinBoard(this.user, chosenBoard);
            else
                displayText("Board already joined!");
        } catch (Exception e) {
            displayText("Invalid invite code!");
        }
        refresh();
    }

    /**
     * Method that disconnects the user and redirect to
     * welcome page
     */
    public void disconnect(){
        mainCtrl.showWelcomePage();
    }

    //endregion

    //region Refresh methods

    /**
     * Method that sets controller for the javaFX object
     * that represent joined board
     * @param boardLoader
     * @param board
     * @return
     */
    private BoardWorkspaceCtrl setCtrl(FXMLLoader boardLoader, Board board){
        BoardWorkspaceCtrl ctrl = boardLoader.getController();
        ctrl.setMainCtrlAndServer(mainCtrl, server, this);
        ctrl.setBoard(board);
        ctrl.setUser(this.user);
        return ctrl;
    }

    /**
     * Method that clears the preview and removes all boards
     * from the preview
     */
    public void clearJoinedBoards(){
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
                boardObject.setUserData(boardWorkspaceLoader.getController());
                setCtrl(boardWorkspaceLoader, board);
                boardsDisplay.getChildren().add(boardObject);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
    //endregion
}
