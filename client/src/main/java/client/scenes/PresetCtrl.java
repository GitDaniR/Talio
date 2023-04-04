package client.scenes;

import commons.Preset;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class PresetCtrl implements Initializable {
    @FXML
    private Label lblName;
    @FXML
    private ColorPicker pickerBackground;
    @FXML
    private ColorPicker pickerForeground;
    @FXML
    private Button btnDefault;
    private Preset preset;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    
}
