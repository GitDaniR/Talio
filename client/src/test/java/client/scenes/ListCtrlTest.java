package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.BoardList;
import commons.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListCtrlTest {

    ListCtrl listCtrl;

    private Board board;

    private BoardList boardList;
    private User user;
    @Mock
    private MainCtrl mainCtrl;
    @Mock
    private ServerUtils serverUtils;


    @BeforeEach
    public void setup() throws Exception{
        listCtrl = new ListCtrl();
        listCtrl.setServerAndCtrl(serverUtils,mainCtrl);
        board = new Board("a","123");
        boardList = new BoardList(0,"title", board ,0);
        boardList.cards = new ArrayList<>();
        listCtrl.setBoardList(boardList);
        user = new User("Name");
        user.unlockedBoards.add(board);
    }
    @Test
    public void testInit(){
        assertDoesNotThrow(()-> listCtrl.initialize(null,null));
    }
    /*@Test
    public void testAddDefaultCard(){
        listCtrl.addDefaultCard();
        Mockito.verify(serverUtils,times(1)).addCard(Mockito.any());
        Mockito.verify(mainCtrl,times(1)).showBoard();
    }*/
    @Test
    public void testDeleteList(){
        when(serverUtils.getBoardByID(0)).thenReturn(board);
        when(serverUtils.getUserByUsername(Mockito.any())).thenReturn(user);
        listCtrl.deleteList();
        Mockito.verify(serverUtils,times(1)).deleteBoardList(0);
    }
    @Test
    public void testEditList(){
        when(serverUtils.getBoardByID(0)).thenReturn(board);
        when(serverUtils.getUserByUsername(Mockito.any())).thenReturn(user);
        listCtrl.editList();
        Mockito.verify(mainCtrl,times(1)).showEditList(listCtrl.getBoardList());
    }
    @Test
    public void testAmountOfCards(){
        assertEquals(listCtrl.getAmountOfCardsInList(),0);
    }
    @Test
    public void testGetId(){
        assertEquals(listCtrl.getListId(),0);
    }


}