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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;

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
        try {
            mainBoard.getChildren().clear();
            var lists = fakeServer.getBoardLists();
            data = FXCollections.observableList(lists);

            for (BoardList currentList : data) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("List.fxml"));
                Node listObject = loader.load();
                ListCtrl listObjectController = loader.getController();
                ///Instantiating a new list to be shown
                listObjectController.setListTitleText(currentList.title);
                //Setting the title of the list
                ObservableList<Card> cardsInList =
                    FXCollections.observableList(fakeServer.getCards(currentList));
//                for (Card currentCard : cardsInList) {
//                    CardCtrl cardObject = new CardCtrl();
//                    //Instantiating a new card to be shown
//                    cardObject.setCardTitleText(currentCard.title);
//                    //Setting the title of the card
//                    listObject.addCardToList(cardObject);
//                    //Adding the card to the list
//                }
                listObjectController.getListAddCardButton().
                        setOnAction(event -> mainCtrl.showAddCard(currentList));
                mainBoard.getChildren().add(listObject);
            }
        } catch (Exception e){
            System.out.print("IO Exception");
        }
    }

    public void disconnectFromServer() {
        mainCtrl.showWelcomePage();
    }
}