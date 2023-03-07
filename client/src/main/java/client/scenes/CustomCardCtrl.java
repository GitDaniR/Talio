package client.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class CustomCardCtrl extends AnchorPane {
    CardCtrl controller;

    public CustomCardCtrl(){
        super();

        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("List.fxml"));
            controller = new CardCtrl();
            loader.setController(controller);
            Node n = loader.load();
            this.getChildren().add(n);
        } catch (IOException ix){}
    }
}
