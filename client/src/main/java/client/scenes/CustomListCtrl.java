package client.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class CustomListCtrl extends AnchorPane {
    ///This class is used to add the list.fxml as an object to JFX
    ///This code was just copy pasted from a tutorial
    ListCtrl controller;
    public CustomListCtrl(){
        super();

        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("List.fxml"));
            controller = new ListCtrl();
            loader.setController(controller);
            Node n = loader.load();
            this.getChildren().add(n);
        } catch (IOException ix){

        }
    }

}
