package client.scenes;

import commons.Card;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class CardCtrl extends AnchorPane implements Initializable{
    @FXML
    private Label cardTitle;
    @FXML
    private Button cardDelete;

    private Card card;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    /** Sets text of the title of the card
     * @param text
     */
    public void setCardTitleText(String text) {
        cardTitle.setText(text);
    }

    public void setCard(Card card){
        this.card=card;
    }
}