package client.scenes;

import client.utils.ServerUtils;
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

import java.net.URL;
import java.util.ResourceBundle;

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
        mainCtrl.showAddList();
    }

    public void refresh() {
        try {
            mainBoard.getChildren().clear();
            var lists = server.getBoardLists();
            data = FXCollections.observableList(lists);

            for (BoardList currentList : data) {
                FXMLLoader listLoader = new FXMLLoader(getClass().getResource("List.fxml"));
                Node listObject = listLoader.load();///Need to also load serverUtils and mainCtrl
                ListCtrl listObjectController = listLoader.getController();
                ///Instantiating a new list
                listObjectController.setListID(currentList.id);
                ///Attaching the id of the list to the listObjectController
                listObjectController.setListTitleText(currentList.title);
                //Setting the title of the list

                ObservableList<Card> cardsInList =
                    FXCollections.observableList(server.getCards());
                for (Card currentCard : cardsInList) {
                    FXMLLoader cardLoader = new FXMLLoader((getClass().getResource("Card.fxml")));
                    Node cardObject = cardLoader.load();
                    CardCtrl cardObjectController = cardLoader.getController();
                    //Instantiating a new card
                    cardObjectController.setCardTitleText(currentCard.title);
                    //Setting the title of the card
                    listObjectController.addCardToList(cardObject);
                    //Adding the card to the list
                }
                ///This can be done with the ID of the list
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