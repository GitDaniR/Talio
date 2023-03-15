package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javax.inject.Inject;
import javafx.scene.control.Button;
import java.net.URL;
import java.util.ResourceBundle;

public class WorkspaceCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private Button createBoard;

    @FXML
    private Button joinBoard;
    @Inject
    public WorkspaceCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void createBoard(){
        mainCtrl.showNewBoard();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
