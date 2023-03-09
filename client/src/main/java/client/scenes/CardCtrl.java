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

public class CardCtrl extends AnchorPane implements Initializable{
    @FXML
    private Label cardTitle;
    @FXML
    private Button cardDelete;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public CardCtrl(){
        super();
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Card.fxml"));
            Node n = loader.load();
            this.getChildren().add(n);
        } catch (IOException ix){}
    }

    /** Sets text of the title of the card
     * @param text
     */
    public void setCardTitleText(String text) {
        cardTitle.setText(text);
    }
}