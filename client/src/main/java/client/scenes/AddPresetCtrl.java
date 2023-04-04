package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Preset;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddPresetCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Board board;
    @FXML
    private TextField presetName;
    @FXML
    private ColorPicker backgroundColorPicker;
    @FXML
    private ColorPicker fontColorPicker;

    @Inject
    public AddPresetCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void setBoard(Board board) {
        this.board = board;
    }

    public void clearFields() {
        presetName.clear();
        backgroundColorPicker.setValue(new Color(1, 1, 1, 1));
        fontColorPicker.setValue(new Color(1, 1, 1, 1));
    }

    public void back() {
        clearFields();
        mainCtrl.showCustomization(board);
    }

    public void save() {
        clearFields();
        String bg = backgroundColorPicker.getValue().toString();
        String font = fontColorPicker.getValue().toString();
        server.addPreset(new Preset(bg, font, new ArrayList<>(), board, board.id));
        mainCtrl.showCustomization(board);
    }
}
