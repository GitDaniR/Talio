package client.scenes;

import client.utils.ServerUtils;
import commons.Preset;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class PresetCtrl implements Initializable {

    private ServerUtils server;
    private MainCtrl mainCtrl;
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

    public void setServerAndMainCtrl(ServerUtils server, MainCtrl mainCtrl){
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void setPreset(Preset preset, int idDefault){
        this.preset = preset;
        lblName.setText(preset.name);
        pickerBackground.setValue(Color.valueOf(preset.backgroundColor));
        pickerForeground.setValue(Color.valueOf(preset.font));
        if(preset.id == idDefault){
            btnDefault.setDisable(true);
        }
    }

    public void setBackground(){
        server.editPresetBackground(preset.id, pickerBackground.getValue().toString());
    }

    public void setForeground(){
        server.editPresetFont(preset.id, pickerForeground.getValue().toString());
    }

    public void deletePreset(){
        server.deletePreset(preset.id);
    }

    public void setDefaultPreset(){
        server.setDefaultPreset(preset.id);
    }

}
