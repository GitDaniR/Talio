package client.scenes;

import client.scenes.models.EditCardModel;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.*;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class EditCardCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private EditCardModel editCardModel;
    @FXML
    private TextField title;
    @FXML
    private TextArea description;
    @FXML
    private Label errorLabel;

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
    private FlowPane tags;
    private ObservableList<Tag> tagsArray;

    @Inject
    public EditCardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.editCardModel = new EditCardModel(server,mainCtrl);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        title.setOnKeyTyped(e -> editCardModel.editCard(cardToEdit.id, getUpdatedCard()));
        description.setOnKeyTyped(e -> editCardModel.editCard(cardToEdit.id, getUpdatedCard()));
    }

    public void subscribeToSocketsEditCardCtrl(){
        editCardModel.startWebSockets(this);
    }

    public void updateCard(Card card){
        if(cardToEdit!=null &&  cardToEdit.id!=card.id)
            return;
        if(!cardToEdit.tags.equals(card.tags)){
            cardToEdit=card;
            setTags();
        }
        cardToEdit=card;
        if(!title.isFocused())
            title.setText(card.title);
        if(!description.isFocused())
            description.setText(card.description);
    }

    public void overwriteTags(List<Tag> tags) {
        if(cardToEdit!=null){
            cardToEdit.tags=tags;
            setTags();
        }
    }

    private PauseTransition delay;

    private void setTextAndRemoveAfterDelay(Label label,String text){
        label.setText(text);
        if(delay!=null)
            delay.stop();//stop previous delay
        delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> {
            label.setText("");
        });
        delay.play();
    }

    public void cancel(){
        if(title.getText().equals("")){
            setTextAndRemoveAfterDelay(errorLabel,"Warning: Card title cannot be left blank!");
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
        Card newCard = new Card(title.getText(),
                description.getText(),
                cardToEdit.index,
                cardToEdit.list,
                cardToEdit.listId);
        newCard.tags=cardToEdit.tags;

        return newCard;
    }

    /**
     * Method that adds Subtask when the add button
     * for subtask is clicked
     */
    public void addSubtask(){
        if(!subtaskTitle.textProperty().get().isEmpty()){
            editCardModel.saveNewSubtask(subtaskTitle.getText(),cardToEdit);
            subtaskTitle.textProperty().set("");
        }
    }

    /**
     * Method that sets subtasks to be the subtasks of the card
     * (this is called when you first edit a card)
     */
    public void setValues() {
        setOldValues();
        setSubtasks();
        setTags();
    }

    public void setOldValues(){
        title.setText(cardToEdit.title);
        description.setText((cardToEdit.description));

    }

    public void setSubtasks(){
        Collections.sort(cardToEdit.subtasks, Comparator.comparingInt(s -> s.index));
        subtasksArray = FXCollections.observableArrayList(cardToEdit.subtasks);
        subtasks.setCellFactory(subtasks1 -> new SubtaskCell(server, mainCtrl));
        subtasks.setItems(subtasksArray);

    }

    /**
     * Method that sets tags to be the tags of the card
     */
    private void setTags(){
        tags.getChildren().clear();
        tagsArray = FXCollections.observableArrayList(cardToEdit.tags);

        for(Tag tag: tagsArray){
            // reusing a scene from the overview
            FXMLLoader tagDisplayLoader = new FXMLLoader(getClass().
                    getResource("TagCellForOverview.fxml"));
            try {
                Node tagObject = tagDisplayLoader.load();
                setCtrl(tagDisplayLoader, tag);
                tags.getChildren().add(tagObject);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Setting controller for a shown tag
     */
    private TagCellForOverviewCtrl setCtrl(FXMLLoader tagLoader, Tag tag){
        TagCellForOverviewCtrl ctrl = tagLoader.getController();
        ctrl.setMainCtrlAndServer(mainCtrl, server);
        ctrl.setTag(tag);
        return ctrl;
    }

    /**
     * Method that sets the card of the subtasks to be the given card,
     * it also sets the handler for the button to add the subtasks to its
     * card through server calls
     * @param cardToEdit - card that subtasks belongs to
     */
    public void setCardToEdit(Card cardToEdit) {
        this.cardToEdit = cardToEdit;
        setValues();
    }

    /**
     * A method to switch scenes to adding/removing tags
     * called when a button is pressed
     */
    public void addRemoveTags(){
        mainCtrl.showAddRemoveTags(cardToEdit);
    }
}

