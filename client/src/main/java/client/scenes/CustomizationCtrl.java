package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.Preset;
import javafx.application.Platform;
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

    public void subscribeToWebsocketsCustomization(){
        server.registerForMessages("/topic/customization/board",Double.class, dummy->
                Platform.runLater(this::loadBoardColors));
        server.registerForMessages("/topic/customization/list",Double.class, dummy->
                Platform.runLater(this::loadListColors));
        server.registerForMessages("/topic/customization/presets",Double.class, dummy->
                Platform.runLater(this::loadPresets));
    }

    public void setBoard(Board board){
        this.board = board;
    }

    public void setValues(){
        loadBoardColors();
        loadListColors();
        loadPresets();
    }

    public void loadBoardColors() {
        colorBoardBackground.setValue(Color.valueOf(board.colorBoardBackground));
        colorBoardFont.setValue(Color.valueOf(board.colorBoardFont));
    }

    public void loadListColors() {
        colorListsBackground.setValue(Color.valueOf(board.colorListsBackground));
        colorListsFont.setValue(Color.valueOf(board.colorListsFont));
    }

    public void loadPresets() {
        List<Preset> presets = getPresets();

        vboxPresets.getChildren().clear();
        if(presets != null) {
            for (Preset p : presets) {
                addPreset(p);
            }
        }
    }

    /** Method to get all presets.
     * @return list of all presets for the current board.
     */
    public List<Preset> getPresets(){
        return server.getAllBoardPresets(board.id);
    }

    public void addPreset(Preset preset){
        FXMLLoader presetLoader = new FXMLLoader(getClass().getResource("Preset.fxml"));
        try {
            Node presetObject = presetLoader.load();
            PresetCtrl presetController = presetLoader.getController();
            presetController.setServerAndMainCtrl(server, mainCtrl);
            presetController.setPreset(preset, getDefaultPresetId());
            vboxPresets.getChildren().add(presetObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO we currently dont have preset saving on
    // server, so once we have itr this method might not be needed
    public int getDefaultPresetId(){
        if(board.defaultCardPreset == null) return 0;
        return board.defaultCardPreset.id;
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

    public void showAddPreset() {
        mainCtrl.showAddPreset(board);
    }
}
