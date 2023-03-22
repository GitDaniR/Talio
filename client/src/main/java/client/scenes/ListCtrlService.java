package client.scenes;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import java.net.URL;
import java.util.ResourceBundle;

public class ListCtrlService implements Initializable {

    public ListCtrl listCtrl;

    private Node listObject;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public ListCtrlService(ListCtrl listCtrl){
        this.listCtrl=listCtrl;
    }

    public void inject() throws Exception{
        FXMLLoader listLoader = new FXMLLoader(getClass().getResource("List.fxml"));
        this.listObject = listLoader.load();
        listCtrl = listLoader.getController();
    }

    public void addCardToList(Node card){
        listCtrl.getCardBox().getChildren().add(card);
    }
}
