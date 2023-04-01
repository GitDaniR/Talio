package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.User;
import javafx.application.Platform;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import suts.WorkspaceAdminCtrlSut;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WorkspaceAdminCtrlTest {

    @Mock
    private ServerUtils server;
    @Mock
    private MainCtrl mainCtrl;

    private List<Board> data;
    private User user;
    private WorkspaceAdminCtrlSut sut;

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
        sut = new WorkspaceAdminCtrlSut(server, mainCtrl, user);

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
        assertNotNull(sut.getBoardsDisplay());
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
    public void disconnect(){
        sut.disconnect();
        verify(mainCtrl, times(1)).showWelcomePage();
    }

    @Test
    public void clearBoards(){
        VBox boardDisplay = new VBox();
        boardDisplay.getChildren().add(new VBox());
        sut.setBoardsDisplay(boardDisplay);

        sut.clearBoards();
        assertEquals(0, sut.getBoardsDisplay().getChildren().size());
    }

    @Test
    public void refresh() {
        sut.refresh();
        verify(server, times(1)).getBoards();
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

}
