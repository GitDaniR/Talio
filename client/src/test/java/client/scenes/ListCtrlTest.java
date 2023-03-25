package client.scenes;

import commons.Board;
import commons.BoardList;
import javafx.application.Platform;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class ListCtrlTest {

    ListCtrl listObjectController;
    ListCtrlService listCtrlService;

    @BeforeEach
    public void setup() throws Exception{
        Platform.setImplicitExit(false);
        Platform.startup(() -> {});
        BoardList currentList = new BoardList("title",
                new Board("blabla","password"),1);
        listCtrlService = new ListCtrlService(new ListCtrl());
        listCtrlService.inject();
    }
    @Test
    public void testAddCard(){
        listCtrlService.addCardToList(new Label());
        assertEquals(1,listCtrlService.listCtrl.
                getCardBox().getChildren().size());
    }


}