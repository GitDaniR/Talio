package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.Person;
import commons.Quote;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;

import java.net.URL;
import java.util.ResourceBundle;

public class AddCardCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private TextField title;
    @FXML
    private TextArea description;


    @Inject
    public AddCardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void save(){
        ok();
    }

    public void cancel(){
        clearFields();
        mainCtrl.showCardOverview();
    }

    private void clearFields() {
        title.clear();
        description.clear();
    }

    // The method uses the getCardFake() instead of getCard() for now
    public void ok() {
        try {
            server.addCardFake(getCardFake());
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        clearFields();
        mainCtrl.showCardOverview();
    }

    // This method is currently not used, as getCardFake is used instead
    // This method also lacks communication with lists (for now)
    private Card getCard() {
        var t = title.getText();
        var d = description.getText();
        return new Card(t, d, getCard().id, null);
    }

    // For now, while the server is not ready, cards are secretly saved as quotes
    // This will be discarded when the server code is up
    private Quote getCardFake() {
        var t = title.getText();
        var d = description.getText();
        var fakePerson = new Person("fakeName", d);

        return new Quote(fakePerson, t);
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                ok();
                break;
            case ESCAPE:
                cancel();
                break;
            default:
                break;
        }
    }
}
