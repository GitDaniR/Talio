package client.scenes;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.BoardList;
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

public class AddCardCtrl implements Initializable {

    //private final FakeServerUtils fakeServer;
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
    private BoardList list = null;

    @Inject
    public AddCardCtrl(ServerUtils server, MainCtrl mainCtrl) {
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
            Card card = getCard();

            server.addCard(getCard());
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

    // Getting the correct index should be done
    private Card getCard() {
        var t = title.getText();
        var d = description.getText();
        if(list == null){
            return new Card(t, d, 0, null, null);
        }else{
            return new Card(t, d, 0, list, this.list.getId());
        }
    }

    public void setList(BoardList list){
        this.list = list;
    }
}