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
import javafx.scene.control.TextField;
import javafx.stage.Modality;

import java.net.URL;
import java.util.ResourceBundle;

public class AddCardCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField title;


    @Inject
    public AddCardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void save(){
        ok();
    }

    public void cancel(){
        clearFields();
        mainCtrl.showCardOverview();
    }

    // may be useful later, when cards will have more fields
    private void clearFields() {
        title.clear();
    }

    public void ok() {
        try {
            server.addQuote(getCardFake());
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

    private Card getCard() {
        var t = title.getText();
        return new Card(t);
    }

    private Quote getCardFake() {
        var t = title.getText();
        var fakePerson = new Person("fakeName", "fakeSurname");

        return new Quote(fakePerson, t);
    }

    /*public void keyPressed(KeyEvent e) {
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
    }*/
}
