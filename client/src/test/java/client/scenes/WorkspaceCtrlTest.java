package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.User;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import suts.WorkspaceCtrlSut;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WorkspaceCtrlTest {

    @Mock
    private ServerUtils server;
    @Mock
    private MainCtrl mainCtrl;

    private List<Board> data;
    private User user;
    private WorkspaceCtrlSut sut;

    @BeforeAll
    public void setup(){
        Platform.setImplicitExit(false);
        Platform.startup(() -> {});
    }

    @BeforeEach
    public void initialize(){
        mainCtrl = mock(MainCtrl.class);
        server = mock(ServerUtils.class);
        data = new ArrayList<>();

        Board firstBoard = new Board("title1", "password1");
        Board secondBoard = new Board("title2", "password2");
        data.add(firstBoard);
        data.add(secondBoard);

        user = new User("username");
        user.id = 1;
        user.boards = data;
        sut = new WorkspaceCtrlSut(server, mainCtrl, user);

    }

    @Test
    public void testConstructors(){
        assertNotNull(sut);
        assertEquals(sut.getUser(), user);
        assertEquals(sut.getUser().username, user.username);
        assertEquals(sut.getUser().id,1);

        assertNotEquals(data, null);
        assertEquals(data.get(0), new Board("title1", "password1"));
        assertEquals(data.get(1), new Board("title2", "password2"));

        assertEquals(sut.getInputBoardToJoin().getText(), "");
        assertEquals(sut.getAlreadyJoinedText().getText(), "");
        assertNotNull(sut.getBoardsDisplay());
        assertNotNull(sut.getDelay());
    }

    @Test
    public void setUser(){
        assertNotNull(sut.getUser());
        String username = "user";
        sut.setUser(username);

        verify(server, times(1)).getUserByUsername(username);
        verify(server, times(1)).addUser(new User(username));
    }

    @Test
    public void createBoard(){
        sut.createBoard();
        verify(mainCtrl, times(1)).showNewBoard(this.user);
    }

    @Test
    public void displayText(){
        String text = "text";
        sut.displayText(text);
        assertEquals(sut.getAlreadyJoinedText().getText(), text);
    }

    @Test
    public void clearInviteText(){
        sut.setInputBoardToJoin(new TextField("myText"));
        sut.clearInviteText();
        assertEquals(sut.getInputBoardToJoin().getText(), "");
    }

    @Test
    public void joinInputBoard() throws Exception {
        sut.setInputBoardToJoin(new TextField("myText#3"));
        sut.joinInputBoard();
        verify(server, times(1)).getBoardByID(3);
    }

    @Test
    public void disconnect(){
        sut.disconnect();
        verify(mainCtrl, times(1)).showWelcomePage();
    }

    @Test
    public void clearJoinedBoards(){
        VBox boardDisplay = new VBox();
        boardDisplay.getChildren().add(new VBox());
        sut.setBoardsDisplay(boardDisplay);

        sut.clearJoinedBoards();
        assertEquals(0, sut.getBoardsDisplay().getChildren().size());
    }

    @Test
    public void refresh() {
        sut.refresh();
        verify(server, times(1)).getUserById(1);
    }

    @Test
    public void getServer() {
        assertEquals(sut.getServer(), server);
    }

    @Test
    public void getMainCtrl() {
        assertEquals(sut.getMainCtrl(), mainCtrl);
    }

    @Test
    public void getUser() {
        assertEquals(sut.getUser(), user);
    }

    @Test
    public void setUserButSetter() {
        User user1 = new User("user1");
        sut.setUser(user1);
        assertEquals(sut.getUser(), user1);
    }

    @Test
    public void getInputBoardToJoin() {
        assertEquals(sut.getInputBoardToJoin().getText(), "");
    }

    @Test
    public void setInputBoardToJoin() {
        TextField input = new TextField("test");
        sut.setInputBoardToJoin(input);
        assertEquals(sut.getInputBoardToJoin(), input);
    }

    @Test
    public void getAlreadyJoinedText() {
        assertEquals(sut.getAlreadyJoinedText().getText(), "");
    }

    @Test
    public void setAlreadyJoinedText() {
        Label input = new Label("test");
        sut.setAlreadyJoinedText(input);
        assertEquals(sut.getAlreadyJoinedText(), input);
    }

    @Test
    public void getBoardsDisplay() {
        assertNotNull(sut.getBoardsDisplay());
        assertEquals(sut.getBoardsDisplay().getChildren().size(), 0);
    }

    @Test
    public void setBoardsDisplay() {
        VBox boardDisplay = new VBox();
        VBox child = new VBox();
        boardDisplay.getChildren().add(child);
        sut.setBoardsDisplay(boardDisplay);

        assertNotNull(sut.getBoardsDisplay());
        assertEquals(sut.getBoardsDisplay().getChildren().get(0), child);
    }

    @Test
    public void getDelay() {
        assertNotNull(sut.getDelay());
    }

    @Test
    public void setDelay() {
        PauseTransition delay = new PauseTransition();
        sut.setDelay(delay);
        assertEquals(sut.getDelay(), delay);
    }


}
