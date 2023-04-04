package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

import javax.inject.Inject;
import java.net.URL;
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
