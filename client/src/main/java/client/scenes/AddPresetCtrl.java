package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class AddPresetCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public AddPresetCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void clearFields() {

    }

    public void cancel() {
        clearFields();
        mainCtrl.showBoard();
    }
}
