package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

import java.net.URL;
import java.util.ResourceBundle;

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
}
