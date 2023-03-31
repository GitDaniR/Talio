package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.User;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import suts.WorkspaceCtrlSut;
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

        user = new User("username");
        sut = new WorkspaceCtrlSut(server, mainCtrl, user);
        data = new ArrayList<>();

        Board firstBoard = new Board("title1", "password1");
        Board secondBoard = new Board("title2", "password2");
        data.add(firstBoard);
        data.add(secondBoard);

    }

    @Test
    public void testConstructors(){
        assertNotNull(sut);
        assertEquals(sut.getUser(), user);
        assertEquals(sut.getUser().username, user.username);

        assertNotEquals(data, null);
        assertEquals(data.get(0), new Board("title1", "password1"));
        assertEquals(data.get(1), new Board("title2", "password2"));

        assertEquals(sut.getInputBoardToJoin().getText(), "");
        assertEquals(sut.getAlreadyJoinedText().getText(), "");

    }

    @Test
    public void setUser(){
        assertNotEquals(sut.getUser(), null);
        String username = "user";
        sut.setUser(username);

        verify(server, times(1)).getUserByUsername(username);
        verify(server, times(1)).addUser(new User(username));

        //assertNotEquals(sut.getUser(), null);
        //assertEquals(sut.getUser().username, username);

    }

    /*@Test
    public void renameBoard(){
        assertEquals(data.get(0).title, "title1");
        sut.renameBoard(data.get(0).id, "title3");
        assertEquals(data.get(0).title, "title3");
    }*/





}
