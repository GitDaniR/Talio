package client.scenes;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.google.inject.Inject;

import client.utils.ServerUtils;
import commons.BoardList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;


import javafx.scene.layout.FlowPane;


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

    }

    public void addList() {
        mainCtrl.showBoard();
        refresh();
    }

    public void refresh() {
        //var lists = server.getLists(); This method will be in antono's code
        var lists = new ArrayList<BoardList>();
        lists.add(new BoardList("Blabla",null));
        data = FXCollections.observableList(lists);
        for(int i=0;i<data.size();i++){
            CustomListCtrl listObject = new CustomListCtrl();
            //listObject.getChildren().add(new Label(data.get(i).title));
            mainBoard.getChildren().add(listObject);
        }
    }
}