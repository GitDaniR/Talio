package client.scenes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ListCtrl extends AnchorPane implements Initializable{
    @FXML
    private Label listTitle;
    @FXML
    private Button listAddCard;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public ListCtrl(){
        super();
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("List.fxml"));
            Node n = loader.load();
            this.getChildren().add(n);
        } catch (IOException ix){

        }
    }

}
