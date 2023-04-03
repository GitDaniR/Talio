package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.BoardList;
import commons.Card;
import commons.Tag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;

public class AddRemoveTagsCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Card cardToEdit;

    @FXML
    private ListView<Tag> tags;
    private ObservableList<Tag> tagsArray;
    private ObservableList<Tag> allTags;

    @Inject
    public AddRemoveTagsCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    /**
     * Method that updates the tag list of the card
     * and comes back to editCard scene
     */
    public void ok() {
        cardToEdit.tags = tagsArray;
        Card toShow = server.editCard(cardToEdit.id, cardToEdit);
        mainCtrl.showEditCard(toShow);
    }

    /**
     * Method that sets tags to be the tags of the card
     */
    public void setTags(){
        tags.getItems().clear();
        BoardList list = server.getBoardListById(cardToEdit.listId);
        int boardId = list.boardId;

        allTags = FXCollections.observableArrayList(server.getTags(boardId));
        tagsArray = FXCollections.observableArrayList(cardToEdit.tags);
        tags.setCellFactory(tags1 -> new TagCellForEditCardCtrl(server, mainCtrl, this));
        tags.setItems(allTags);
    }

    /**
     * Method that sets the card that is being edited
     * @param cardToEdit - card that tags belong to
     */
    public void setCardToEdit(Card cardToEdit) {
        this.cardToEdit = cardToEdit;
    }

    public Card getCardToEdit(){
        return cardToEdit;
    }

    /**
     * Adds a tag to the list of tags the card has
     * @param tag to be added
     */
    public void addTag(Tag tag) {
        if(!tagsArray.contains(tag)) tagsArray.add(tag);
    }

    /**
     * Removes a tag from the list of tags the card has
     * @param tag to be removed
     */
    public void removeTag(Tag tag) {
        tagsArray.remove(tag);
    }
}

