package client.scenes;

import commons.Board;
import commons.BoardList;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    private ListCtrl createListObject(FXMLLoader listLoader, BoardList currentList){
        listObjectController = listLoader.getController();
        ///Instantiating a new list
        listObjectController.setBoardList(currentList);
        ///Attaching the boardList object to the listObjectController
        listObjectController.setListTitleText(currentList.title);
        //Setting the title of the list
        //listObjectController.setServerAndCtrl(server,mainCtrl);
        //Setting the server and  ctrl because I have no idea how to inject it
        //listObjectController.getListAddCardButton().
        //        setOnAction(event -> mainCtrl.showAddCard(currentList));
        //Telling the button what to do
        return listObjectController;
    }

    @BeforeEach
    public void setup(){
        BoardList currentList = new BoardList("title",new Board("blabla","password"),1);
        try{
            FXMLLoader listLoader = new FXMLLoader(getClass().getResource("List.fxml"));
            Node listObject = listLoader.load();
            ListCtrl listObjectController = createListObject(listLoader,currentList);
        } catch (Exception e){
            System.out.print(e.toString());
        }
    }

    @Test
    public void testAddCardToList(){
        listObjectController.addCardToList(new Label("test"));
        assertEquals(listObjectController.getChildren().size(),1);
    }

}