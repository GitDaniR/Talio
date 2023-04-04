package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.Preset;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CustomizationCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Board board;

    @FXML
    private ColorPicker colorBoardBackground;
    @FXML
    private ColorPicker colorBoardFont;
    @FXML
    private ColorPicker colorListsBackground;
    @FXML
    private ColorPicker colorListsFont;
    @FXML
    private VBox vboxPresets;

    @Inject
    public CustomizationCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void setBoard(Board board){
        this.board = board;
    }

    public void setValues(){
        colorBoardBackground.setValue(Color.valueOf(board.colorBoardBackground));
        colorBoardFont.setValue(Color.valueOf(board.colorBoardFont));
        colorListsBackground.setValue(Color.valueOf(board.colorListsBackground));
        colorListsFont.setValue(Color.valueOf(board.colorListsFont));
        loadPresets();
    }

    public void loadPresets(){
        //TODO actually load from server
        List<Preset> presets = new ArrayList<>();
        presets.add(new Preset(0, "0xff0000", "0x00ffff", null, "Preset 1", null, 0));
        presets.add(new Preset(1, "0x00ff00", "0xff00ff", null, "Preset 2", null, 0));
        presets.add(new Preset(2, "0x0000ff", "0xffff00", null, "Preset 3", null, 0));

        vboxPresets.getChildren().clear();
        if(presets != null) {
            for (Preset p : presets) {
                addPreset(p);
            }
        }
    }

    public void addPreset(Preset preset){
        FXMLLoader presetLoader = new FXMLLoader(getClass().getResource("Preset.fxml"));
        try {
            Node presetObject = presetLoader.load();
            PresetCtrl presetController = presetLoader.getController();
            presetController.setPreset(preset, getDefaultPresetId());
            vboxPresets.getChildren().add(presetObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO we currently dont have preset saving on server, so once we have itr this method might not be needed
    public int getDefaultPresetId(){
        if(board.defaultCardPreset == null) return 0;
        return getDefaultPresetId();
    }

    public void resetBoard(){
        colorBoardBackground.setValue(Color.ANTIQUEWHITE);
        colorBoardFont.setValue(Color.BLACK);

        server.editColorBoardBackground(board.id, colorBoardBackground.getValue().toString());
        server.editColorBoardFont(board.id, colorBoardFont.getValue().toString());
    }

    public void resetLists(){
        colorListsBackground.setValue(Color.LAVENDER);
        colorListsFont.setValue(Color.BLACK);

        server.editColorListsBackground(board.id, colorListsBackground.getValue().toString());
        server.editColorListsFont(board.id, colorListsFont.getValue().toString());
    }

    public void back(){
        mainCtrl.showBoard(board);
    }

    public void save() {
        server.editColorBoardBackground(board.id, colorBoardBackground.getValue().toString());
        server.editColorBoardFont(board.id, colorBoardFont.getValue().toString());
        server.editColorListsBackground(board.id, colorListsBackground.getValue().toString());
        server.editColorListsFont(board.id, colorListsFont.getValue().toString());

        mainCtrl.showBoard(board);
    }
}
