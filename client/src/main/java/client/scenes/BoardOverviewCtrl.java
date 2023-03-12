package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
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

    private Board board;

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
        board = server.getBoardByID(1);
        //Setting the first board as the main board
        //I tried to get the first boards of all boards but didn't work
    }

    public void addList() {
        mainCtrl.showAddList(board);
    }

    /**
     * @param listLoader the loader to load @FXML contents
     * @param currentList the list to be associated to the controller
     * @return the list controller with the associated boardList
     */
    private ListCtrl createListObject(FXMLLoader listLoader, BoardList currentList){
        ListCtrl listObjectController = listLoader.getController();
        ///Instantiating a new list
        listObjectController.setBoardList(currentList);
        ///Attaching the boardList object to the listObjectController
        listObjectController.setListTitleText(currentList.title);
        //Setting the title of the list
        listObjectController.setServerAndCtrl(server,mainCtrl);
        //Setting the server and  ctrl because I have no idea how to inject it
        return listObjectController;
    }

    /**
     * @param cardLoader the loader to load @FXML contents
     * @param currentCard the card to be associated to the ctrl
     * @return the cardCtrl with the associated card
     */
    private CardCtrl createCardObject(FXMLLoader cardLoader, Card currentCard){
        CardCtrl cardObjectController = cardLoader.getController();
        //Instantiating a new card
        cardObjectController.setCard(currentCard);
        ///Attaching the card object to the cardCtrl
        cardObjectController.setCardTitleText(currentCard.title);
        //Setting the title of the card
        return cardObjectController;
    }

    public void refresh() {
        try {
            mainBoard.getChildren().clear();
            var lists = server.getBoardLists();
            data = FXCollections.observableList(lists);
            for (BoardList currentList : data) {
                FXMLLoader listLoader = new FXMLLoader(getClass().getResource("List.fxml"));
                Node listObject = listLoader.load();
                ListCtrl listObjectController = createListObject(listLoader,currentList);
                ObservableList<Card> cardsInList =
                    FXCollections.observableList(server.getCards(currentList.id));
                for (Card currentCard : cardsInList) {
                    FXMLLoader cardLoader = new FXMLLoader((getClass().getResource("Card.fxml")));
                    Node cardObject = cardLoader.load();
                    CardCtrl cardObjectController = createCardObject(cardLoader,currentCard);
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