package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class HelpCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public HelpCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void back(){
        mainCtrl.showWelcomePage();
    }
}
