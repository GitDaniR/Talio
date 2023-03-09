package client.scenes;

import javafx.fxml.FXML;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;


import java.net.URL;
import java.util.ResourceBundle;

public class ListCtrl extends AnchorPane implements Initializable{
    @FXML
    private Label listTitle;
    @FXML
    private VBox cardBox;
    @FXML
    private Button listAddCard;
    @FXML
    private Button listCloseButton;
    @FXML
    private Button listEditButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    /** Sets the text of the title of the list
     * @param text
     */
    public void setListTitleText(String text) {
        listTitle.setText(text);
    }

    public void addCardToList(Node card){
        cardBox.getChildren().add(card);
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
    public Button getListAddCardButton() {
        return listAddCard;
    }
}
