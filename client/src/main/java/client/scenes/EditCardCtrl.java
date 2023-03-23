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
     * @param oldCard the old version
     * @return a new version of the card
     */
    private Card getUpdatedCard(Card oldCard) {
        var t = title.getText();
        var d = description.getText();

        oldCard.title = t;
        oldCard.description = d;

        return oldCard;
    }

    /**
     * Method that returns the subtask with the title from the text box
     * which is added to the database through the server
     * @return
     */
    private Subtask getNewSubtask(){
        Subtask subtaskEntity = new Subtask(subtaskTitle.getText(), false,
               cardToEdit.subtasks.size(),cardToEdit);
        return server.addSubtask(subtaskEntity, cardToEdit);

    }

    /**
     * Method that sets subtasks to be the subtasks of the card
     */
    public void setSubtasks(){
        subtasksArray = FXCollections.observableArrayList(cardToEdit.subtasks);
        subtasks.setCellFactory(subtasks1 -> new SubtaskCell(server, mainCtrl));
        subtasks.setItems(subtasksArray);

        addSubtask.setOnAction(event -> {
            if(!subtaskTitle.textProperty().get().isEmpty()){
                Subtask subtaskEntity = getNewSubtask();
                subtasksArray.add(subtaskEntity);
                subtaskTitle.textProperty().set("");
            }
        });

    }

    /**
     * Method that sets the card of the subtasks to be the given card,
     * it also sets the handler for the button to add the subtasks to its
     * card through server calls
     * @param cardToEdit - card that subtasks belongs to
     */
    public void setCardToEdit(Card cardToEdit) {
        this.cardToEdit = cardToEdit;
        setSubtasks();
    }

    public Timer startTimer(int refreshRate){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(()->{
                    refresh();
                });
            }
        }, 0, refreshRate);
        return timer;
    }

    public void refresh(){
        int id = cardToEdit.id;
        try{
            //fetch the card from the server
            cardToEdit = server.getCardById(id);
            // adjust Subtasks
            //THIS SHOULD BE UNCOMMENTED WHEN WE HAVE ENDPOINTS
            //setSubtasks();
            oldTitle.setText(cardToEdit.title);
            oldDescription.setText(cardToEdit.description);
        }catch(Exception e){
            //if it doesn't exist someone probably deleted it while we were editing the card
            //so we are returned to the board overview
            e.printStackTrace();
            mainCtrl.showBoard();
        }
    }
}
