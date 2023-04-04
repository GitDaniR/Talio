package client.scenes;

import client.utils.ServerUtils;
import commons.Card;
import commons.Preset;
import commons.Subtask;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class CardCtrl extends AnchorPane implements Initializable {

    @FXML
    private HBox container;

    @FXML
    private Label cardTitle;

    @FXML
    private Button cardDelete;
    @FXML
    private ImageView imgDescription;
    @FXML
    private Label lblSubtasks;
    @FXML
    private FlowPane paneTags;

    private Card card;

    private ServerUtils server;
    private MainCtrl mainCtrl;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    /**
     * Sets the card to be represented (if it changed)
     * the title and the description
     */
    public void setCardAndAttributes(Card card) {
        this.card = server.getCardById(card.id);
        cardTitle.setText(card.title);
        if(card.description == null || card.description.isEmpty()) {
            imgDescription.setVisible(false);
        } else{
            imgDescription.setVisible(true);
        }
        if(card.subtasks == null || card.subtasks.isEmpty()){
            lblSubtasks.setVisible(false);
        }else{
            long total = card.subtasks.size();
            long done = Stream.of(card.subtasks.toArray()).
                filter(subtask->((Subtask)subtask).done).count();
            lblSubtasks.setText("(" + done + "/" + total + "Done)");
            lblSubtasks.setVisible(true);
        }
        paneTags.getChildren().clear();
        if(card.tags != null){
            for(Tag tag: card.tags){
                addTag(tag);
            }
        }
        setFontAndBackgroundColor();
    }

    /** This method associates a card to the controller for easy access
     * @param card the card to be associated with the controller
     */
    public void setCard(Card card){
        this.card = card;
    }

    public void deleteCard(){
        server.deleteCard(card.id);
    }

    public void editCard(){
        mainCtrl.showEditCard(card);
    }

    public void setServerAndCtrl(ServerUtils server, MainCtrl mainCtrl){
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public int getCardId() {
        return card.id;
    }
    public void addTag(Tag tag){
        FXMLLoader tagLoader = new FXMLLoader(getClass().getResource("TagIcon.fxml"));
        try {
            Node tagObject = tagLoader.load();
            TagIconController tagController = tagLoader.getController();
            tagController.setTag(tag);
            paneTags.getChildren().add(tagObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method which sets the fond and backgrounds colors of
     * the CardCtrl based on the Preset the current Card
     * is pointing to.
     */
    public void setFontAndBackgroundColor(){
        Preset preset = server.getPresetById(card.presetId);
        String fontColor = preset.font;
        String backgroundColor = preset.backgroundColor;
        cardTitle.setTextFill(Paint.valueOf(fontColor));
        String style = "-fx-border-color: black; -fx-border-width: 4; -fx-border-radius: 5 5 5 5;" +
                "-fx-background-radius: 5 5 5 5; -fx-background-color:" +
                backgroundColor.replace("0x", "#");
        container.setStyle(style);
    }
}