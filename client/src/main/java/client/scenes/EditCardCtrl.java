package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import jakarta.ws.rs.WebApplicationException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

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

        return new Card(t, d, oldCard.index, oldCard.list, oldCard.listId);
    }

    public void setCardToEdit(Card cardToEdit) {
        this.cardToEdit = cardToEdit;
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
