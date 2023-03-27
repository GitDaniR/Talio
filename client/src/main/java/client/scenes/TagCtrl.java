package client.scenes;

import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class TagCtrl implements Initializable {
    @FXML
    private Text txtLetter;
    @FXML
    private Ellipse background;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
