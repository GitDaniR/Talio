package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.Subtask;
import jakarta.ws.rs.WebApplicationException;
import javafx.application.Platform;
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

    @Inject
    public EditCardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        server.registerForMessages("/topic/subtasks", Integer.class, cardId -> {
            Platform.runLater(() -> overwriteSubtasks(server.getCardById(cardId).subtasks));
        });
        server.registerForMessages("/topic/cards/rename", Card.class, card -> {
            if(Objects.equals(card.id, cardToEdit.id))
                Platform.runLater(() -> updateCard(card));
        });
        title.setOnKeyTyped(e -> server.editCard(cardToEdit.id, getUpdatedCard()));
        description.setOnKeyTyped(e -> server.editCard(cardToEdit.id, getUpdatedCard()));
    }

    private void updateCard(Card card){
        cardToEdit=card;
        if(!title.isFocused())
            title.setText(card.title);
        if(!description.isFocused())
            description.setText(card.description);
    }

    private void overwriteSubtasks(List<Subtask> t){
        cardToEdit.subtasks=t;
        Collections.sort(cardToEdit.subtasks, Comparator.comparingInt(s -> s.index));
        subtasksArray = FXCollections.observableArrayList(t);
        subtasks.setItems(subtasksArray);
    }

    public void cancel(){
        clearFields();
        mainCtrl.showBoard();
    }

    public void ok() {
        try {
            server.editCard(cardToEdit.id, getUpdatedCard());
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

    private void clearFields() {
        title.clear();
        description.clear();
    }

    /**
     * Makes a card with updated title and description
     * @return a new version of the card
     */
    private Card getUpdatedCard() {
        var t = title.getText();
        var d = description.getText();

        return new Card(t,d, cardToEdit.index, cardToEdit.list, cardToEdit.listId);
    }

    /**
     * Method that returns the subtask with the title from the text box
     * which is added to the database through the server
     * @return generated subtask
     */
    private Subtask generateNewSubtask(){
        Subtask subtaskEntity = new Subtask(subtaskTitle.getText(), false,
               cardToEdit.subtasks.size(),cardToEdit);
        cardToEdit.subtasks.add(subtaskEntity);
        return server.addSubtask(subtaskEntity);
    }

    /**
     * Method that adds Subtask when the add button
     * for subtask is clicked
     */
    public void addSubtask(){
        if(!subtaskTitle.textProperty().get().isEmpty()){
            generateNewSubtask();
            subtaskTitle.textProperty().set("");
        }
        cardToEdit = server.getCardById(cardToEdit.getId());

    }

    /**
     * Method that sets subtasks to be the subtasks of the card
     * (this is called when you first edit a card)
     */
    public void setSubtasksAndOldValues() {
        title.setText(cardToEdit.title);
        description.setText((cardToEdit.description));

        Collections.sort(cardToEdit.subtasks, Comparator.comparingInt(s -> s.index));
        subtasksArray = FXCollections.observableArrayList(cardToEdit.subtasks);
        subtasks.setCellFactory(subtasks1 -> new SubtaskCell(server, mainCtrl));
        subtasks.setItems(subtasksArray);
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
}
