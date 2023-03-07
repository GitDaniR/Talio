package client.scenes;

import java.net.URL;
import java.util.ResourceBundle;

import com.google.inject.Inject;

import client.utils.ServerUtils;
import commons.BoardList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;


import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;


public class BoardOverviewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private ObservableList<BoardList> data;

    @FXML
    private FlowPane mainBoard;

    @Inject
    public BoardOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refresh();
    }

    public void addList() {
        mainCtrl.showAdd();
    }

    public void refresh() {
        mainBoard.getChildren().clear();
        var lists = server.getBoardLists();
        data = FXCollections.observableList(lists);
        for(BoardList currentList : data ){
            ListCtrl listObject = new ListCtrl(); ///Instantiating a new list to be shown
            Label listTitle = ((Label)((VBox) listObject.getChildren().get(0)).getChildren().get(0)); ///the title of the list
            listTitle.setText(currentList.title);
            //listObject.getChildren().add(new Label(currentList.title));
            mainBoard.getChildren().add(listObject);
        }
    }
}