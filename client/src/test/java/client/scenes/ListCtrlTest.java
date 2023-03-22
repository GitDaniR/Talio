package client.scenes;

import commons.Board;
import commons.BoardList;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

class ListCtrlTest implements Initializable {

    ListCtrl listObjectController;
    ListCtrlService listCtrlService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @BeforeEach
    public void setup() throws Exception{
        Platform.startup(() -> {});
        BoardList currentList = new BoardList("title",new Board("blabla","password"),1);
        listCtrlService = new ListCtrlService(new ListCtrl());
        listCtrlService.inject();
    }

    @Test
    public void test(){
        System.out.print("Yay!");
    }
    @Test
    public void testAddCard(){
        listCtrlService.addCardToList(new Label());
        assertEquals(1,listCtrlService.listCtrl.getCardBox().getChildren().size());
    }


}