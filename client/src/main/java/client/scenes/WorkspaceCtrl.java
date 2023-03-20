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

import javax.inject.Inject;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

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
    @FXML private TextField txtBoardName;
    @FXML
    private Label alreadyJoinedText;

    @Inject
    public WorkspaceCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
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

    public void createBoard(){
        mainCtrl.showNewBoard(this.user);
    }

    public void displayAlreadyJoinedText(){
        alreadyJoinedText.setText("Board already joined!");
        // message gets deleted after 2 seconds
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> {
            alreadyJoinedText.setText("");
        });
        delay.play();
    }

    public void joinInputBoard() throws Exception {
        // Take the ID out of inputBoardToJoin (integer after "#")
        String[] boardToJoin = inputBoardToJoin.getText().split("#");
        int boardToJoinId;

        try {
            boardToJoinId = Integer.parseInt(boardToJoin[1]);
            Board chosenBoard = server.getBoardByID(boardToJoinId); // Take the board with that ID
            if(!user.hasBoardAlready(chosenBoard.id))
                mainCtrl.joinBoard(this.user, chosenBoard);
            else
                displayAlreadyJoinedText();
        } catch (Exception e) {
            throw new Exception("Invalid input");
        }
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
    public void initialize(URL location, ResourceBundle resources) {}

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
                setCtrl(boardWorkspaceLoader, board);
                boardsDisplay.getChildren().add(boardObject);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
