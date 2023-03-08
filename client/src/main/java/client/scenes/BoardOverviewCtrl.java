package client.scenes;

import java.net.URL;
import java.util.*;

import client.utils.FakeServerUtils;
import com.google.inject.Inject;
import commons.BoardList;
import commons.Card;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BoardOverviewCtrl implements Initializable {

    private final FakeServerUtils fakeServer;
    private final MainCtrl mainCtrl;

    private ObservableList<BoardList> data;

    @FXML
    private FlowPane mainBoard;

    @Inject
    public BoardOverviewCtrl(FakeServerUtils fakeServer, MainCtrl mainCtrl) {
        this.fakeServer = fakeServer;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fakeServer.setup();
        refresh();
    }

    public void addList() {
        mainCtrl.showAddList();
    }

    public void refresh() {
        mainBoard.getChildren().clear();
        var lists = fakeServer.getBoardLists();
        data = FXCollections.observableList(lists);

        for(BoardList currentList : data ){
            ListCtrl listObject = new ListCtrl(); ///Instantiating a new list to be shown
            Label listTitle = ((Label)((VBox) listObject.getChildren().get(0)).getChildren().get(0)); ///the title of the list
            listTitle.setText(currentList.title);

            ObservableList<Card> cardsInList = FXCollections.observableList(fakeServer.getCards(currentList));
            for(Card currentCard : cardsInList ) {
                CardCtrl cardObject = new CardCtrl(); ///Instantiating a new card to be shown
                Label cardTitle = ((Label) ((HBox) cardObject.getChildren().get(0)).getChildren().get(0)); ///the title of the card
                cardTitle.setText(currentCard.title);

                ((VBox)((VBox) listObject.getChildren().get(0)).getChildren().get(1)).getChildren().add(cardObject);
            }

            ((Button)((VBox) listObject.getChildren().get(0)).getChildren().get(2)).setOnAction(event -> {
                mainCtrl.showAddCard(currentList);
            });

            //listObject.getChildren().add(new Label(currentList.title));
            mainBoard.getChildren().add(listObject);
        }
    }

    public void disconnectFromServer() {
        mainCtrl.showWelcomePage();
    }
}