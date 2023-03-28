package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.*;
import jakarta.ws.rs.WebApplicationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;
import java.net.URL;
import java.util.*;

public class EditCardCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private TextField title;
    @FXML
    private TextArea description;
    @FXML
    private Label oldTitle;
    @FXML
    private Label oldDescription;

    @FXML
    private Button save;
    @FXML
    private Button cancel;
    private Card cardToEdit;

    @FXML
    private ListView<Subtask> subtasks;
    @FXML
    private TextField subtaskTitle;
    @FXML
    private Button addSubtask;
    private ObservableList<Subtask> subtasksArray;

    @FXML
    private ListView<Tag> tags;
    private ObservableList<Tag> tagsArray;
    private ObservableList<Tag> allTags;

    @Inject
    public EditCardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void cancel(){
        clearFields();
        mainCtrl.showBoard();
    }

    private void clearFields() {
        title.clear();
        description.clear();
    }

    public void ok() {
        try {
            server.editCard(cardToEdit.id, getUpdatedCard(cardToEdit));
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        clearFields();
        mainCtrl.showBoard();
    }

    /**
     * Makes a card with updated title and description
     * @return a new version of the card
     */
    private Card getUpdatedCard(Card card) {
        card.title = title.getText();
        card.description = description.getText();
        card.tags = tagsArray;

        return card;
    }

    /**
     * Method that returns the subtask with the title from the text box
     * which is added to the database through the server
     * @return
     */
    private Subtask getNewSubtask(){

        Subtask subtaskEntity = new Subtask(subtaskTitle.getText(), false,
               cardToEdit.subtasks.size(),cardToEdit);
        return server.addSubtask(subtaskEntity);
    }

    /**
     * Method that sets subtasks to be the subtasks of the card
     */
    public void setSubtasksAndOldValues(){
        oldTitle.setText(cardToEdit.title);
        oldDescription.setText((cardToEdit.description));

        subtasksArray = FXCollections.observableArrayList(cardToEdit.subtasks);
        subtasks.setCellFactory(subtasks1 -> new SubtaskCell(server, mainCtrl));
        subtasks.setItems(subtasksArray);

        int boardId = server.getBoardListById(cardToEdit.listId).boardId;
        allTags = FXCollections.observableArrayList(server.getTags(boardId));
        tagsArray = FXCollections.observableArrayList(cardToEdit.tags);
        tags.setCellFactory(tags1 -> new TagCellForEditCardCtrl(server, mainCtrl, this));
        tags.setItems(allTags);
    }

    /**
     * Method that adds Subtask when the add button
     * for subtask is clicked
     */
    public void addSubtask(){
        if(!subtaskTitle.textProperty().get().isEmpty()){
            Subtask subtaskEntity = getNewSubtask();
            subtasksArray.add(subtaskEntity);
            subtaskTitle.textProperty().set("");
        }
        cardToEdit = server.getCardById(cardToEdit.getId());

    }

    /**
     * Method that sets the card of the subtasks to be the given card,
     * it also sets the handler for the button to add the subtasks to its
     * card through server calls
     * @param cardToEdit - card that subtasks belongs to
     */
    public void setCardToEdit(Card cardToEdit) {
        this.cardToEdit = cardToEdit;
        setSubtasksAndOldValues();
    }

    public Card getCardToEdit(){
        return cardToEdit;
    }

    public void addTag(Tag tag) {
        if(!tagsArray.contains(tag)) tagsArray.add(tag);
        tag = server.getTagById(tag.id);
        cardToEdit.addTag(tag);
    }

    public void removeTag(Tag tag) {
        tagsArray.remove(tag);
        tag = server.getTagById(tag.id);
        cardToEdit.removeTag(tag);
    }
}