package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.*;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class EditCardCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    public AnchorPane everything;
    @FXML
    private TextField title;
    @FXML
    private TextArea description;
    @FXML
    private Label errorLabel;
    private Card cardToEdit;
    @FXML
    private ListView<Subtask> subtasks;
    @FXML
    private TextField subtaskTitle;
    private ObservableList<Subtask> subtasksArray;
    @FXML
    private FlowPane tags;
    private ObservableList<Tag> tagsArray;
    @FXML
    private Label currentPreset;
    @FXML
    private ComboBox presetMenu;
    @FXML
    private Label readOnlyLabel;

    @Inject
    public EditCardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        title.setOnKeyTyped(e -> server.editCard(cardToEdit.id, getUpdatedCard()));
        description.setOnKeyTyped(e -> server.editCard(cardToEdit.id, getUpdatedCard()));
        mainCtrl.consumeShortcutsTextField(title);
        mainCtrl.consumeShortcutsTextField(description);
        mainCtrl.consumeShortcutsTextField(subtaskTitle);

        // add keyboard shortcuts
        everything.addEventFilter(KeyEvent.KEY_PRESSED, (EventHandler<KeyEvent>) keyEvent -> {
            switch (keyEvent.getCode()) {
                case ESCAPE:
                    cancel();
                    keyEvent.consume();
                    break;
                default:
                    keyEvent.consume();
                    break;
            }
        });

        // Cell factory
        presetMenu.setCellFactory(lv -> new ListCell<Preset>() {
            @Override
            public void updateItem(Preset item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(item.toString());
                    setDisable(item.getId() == cardToEdit.presetId);
                }
            }
        });

        // Add listener to presetMenu to detect when a Preset is selected.
        presetMenu.valueProperty().addListener((obs, oldval, newval) -> {
            if (newval != null) {
                Preset p = (Preset) newval;
                cardToEdit.setPreset(p);
                server.editCard(cardToEdit.id, cardToEdit);
            }
        });
    }

    public void subscribeToSocketsEditCardCtrl(){
        server.registerForMessages("/topic/subtasks", Integer.class, cardId -> {
            Platform.runLater(() -> setCardToEdit(server.getCardById(cardId)));
        });
        server.registerForMessages("/topic/cards/rename", Card.class, card -> {
            if(Objects.equals(card.id, cardToEdit.id))
                Platform.runLater(() -> updateCard(card));
        });
        server.registerForMessages("/topic/tags", Integer.class, boardId -> {
            Platform.runLater(() -> overwriteTags(server.getBoardByID(boardId).tags));
        });
        server.registerForMessages("/topic/cards", Integer.class, cardId ->{
            if(cardToEdit.id==cardId)
                Platform.runLater(mainCtrl::showBoard);
        });
    }

    private boolean hasWriteAccess() {
        Board b = server.getBoardByID(server.getBoardListById(cardToEdit.listId).boardId);
        return mainCtrl.getIsAdmin() || b.password.equals("") || b.password.equals("NO_PASSWORD") ||
                server.getUserByUsername(mainCtrl.getUsername()).unlockedBoards.contains(b);
    }

    private static void throwWriteAlert() {
        var alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setContentText("You don't have write access!");
        alert.showAndWait();
    }


    private void updateCard(Card card){
        if(!cardToEdit.tags.equals(card.tags)){
            cardToEdit=card;
            setTags();
        }
        cardToEdit=card;
        if(!title.isFocused())
            title.setText(card.title);
        if(!description.isFocused())
            description.setText(card.description);
        updatePresetMenu();
    }

    private void overwriteTags(List<Tag> tags) {
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
        newCard.presetId = cardToEdit.presetId;

        return newCard;
    }

    /**
     * Method that returns the subtask with the title from the text box
     * which is added to the database through the server
     * @return generated subtask
     */
    private Subtask saveNewSubtask(){
        Subtask subtaskEntity = new Subtask(subtaskTitle.getText(), false,
                cardToEdit.subtasks.size(),cardToEdit);
        return server.addSubtask(subtaskEntity);
    }

    /**
     * Method that adds Subtask when the add button
     * for subtask is clicked
     */
    public void addSubtask(){
        if(!hasWriteAccess()){
            throwWriteAlert();
            return;
        }
        if(!subtaskTitle.textProperty().get().isEmpty()){
            saveNewSubtask();
            subtaskTitle.textProperty().set("");
        }
        //cardToEdit = server.getCardById(cardToEdit.getId());
        //setValues();

    }

    /**
     * Method that sets subtasks to be the subtasks of the card
     * (this is called when you first edit a card)
     */
    public void setValues() {
        setOldValues();
        setSubtasks();
        setTags();
        updatePresetMenu();
    }

    public void setOldValues(){
        title.setText(cardToEdit.title);
        description.setText((cardToEdit.description));
        title.setDisable(false);
        description.setDisable(false);
        if(!hasWriteAccess()){
            title.setDisable(true);
            description.setDisable(true);
        }
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
        readOnlyLabel.setVisible(!hasWriteAccess());
        setValues();
    }

    /**
     * A method to switch scenes to adding/removing tags
     * called when a button is pressed
     */
    public void addRemoveTags(){
        if(!hasWriteAccess()){
            throwWriteAlert();
            return;
        }
        mainCtrl.showAddRemoveTags(cardToEdit);
    }

    public void updatePresetMenu() {
        // TO-DO: find easier way of retrieving presets for a board.
        BoardList list = server.getBoardListById(cardToEdit.listId);
        // Make ObservableList for presets
        ObservableList<Preset> presets = FXCollections.observableArrayList();
        // Fetch presets from DB and add to OL
        presets.addAll(server.getAllBoardPresets(list.boardId));
        presetMenu.setItems(presets);
        presetMenu.setDisable(!hasWriteAccess());
    }
}
