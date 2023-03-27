package client.scenes;

import client.utils.ServerUtils;
import commons.Card;
import commons.Subtask;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class CardCtrl extends AnchorPane implements Initializable{
    @FXML
    private Label cardTitle;
    @FXML
    private Button cardEdit;
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
        this.card = card;
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
        if(card.tags != null){
            for(Tag tag: card.tags){
                addTag(tag);
            }
        }
        //TODO
        // these are here to verify that displaying tags
        // works since we don't have the backend for it yet.
        // Once it all works fine remove these sample tags.
        addTag(new Tag("Client", "0x00FF80"));
        addTag(new Tag("Server", "0xFF0000"));
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
        FXMLLoader tagLoader = new FXMLLoader(getClass().getResource("Tag.fxml"));
        try {
            Node tagObject = tagLoader.load();
            TagCtrl tagController = tagLoader.getController();
            tagController.setTag(tag);
            paneTags.getChildren().add(tagObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}