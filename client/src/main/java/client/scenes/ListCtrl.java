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
    @FXML
    private Button listCloseButton;
    @FXML
    private Button listEditButton;

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

    /**
     * @return the title of the list
     */
    public Label getListTitle() {
        return listTitle;
    }

    /**
     * @return the Edit button of the list
     */
    public Button getListEditButton() {
        return listEditButton;
    }

    /**
     * @return the X button for the list
     */
    public Button getListCloseButton() {
        return listCloseButton;
    }

    /**
     * @return the list button of the list
     */
    public Button getListAddCard() {
        return listAddCard;
    }
}
