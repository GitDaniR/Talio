package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.BoardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ListCtrlTest {

    ListCtrl listCtrl;
    @Mock
    private MainCtrl mainCtrl;
    @Mock
    private ServerUtils serverUtils;


    @BeforeEach
    public void setup() throws Exception{
        BoardList currentList = new BoardList("title",
                new Board("blabla","password"),1);
        listCtrl = new ListCtrl();
        listCtrl.setServerAndCtrl(serverUtils,mainCtrl);
        BoardList boardList = new BoardList(0,"title",new Board("a","123"),0);
        boardList.cards = new ArrayList<>();
        listCtrl.setBoardList(boardList);
    }
    @Test
    public void testInit(){
        assertDoesNotThrow(()-> listCtrl.initialize(null,null));
    }
    @Test
    public void testAddDefaultCard(){
        listCtrl.addDefaultCard();
        Mockito.verify(serverUtils,times(1)).addCard(Mockito.any());
        Mockito.verify(mainCtrl,times(1)).showBoard();
    }
    @Test
    public void testDeleteList(){
        listCtrl.deleteList();
        Mockito.verify(serverUtils,times(1)).deleteBoardList(0);
    }
    @Test
    public void testEditList(){
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