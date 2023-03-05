package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.BoardList;
import commons.Quote;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class BoardOverviewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private ObservableList<BoardList> data;

    @FXML
    private TableView<BoardList> table;
    @FXML
    private TableColumn<Quote, String> colFirstName;
    @FXML
    private TableColumn<Quote, String> colLastName;
    @FXML
    private TableColumn<Quote, String> colQuote;

    @Inject
    public BoardOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void addList() {
        mainCtrl.showAdd();
    }

    public void refresh() {
        var lists = server.getBoardLists();
        data = FXCollections.observableList(lists);
        table.setItems(data);
    }

}
