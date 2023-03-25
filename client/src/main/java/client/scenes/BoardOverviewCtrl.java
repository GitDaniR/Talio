package client.scenes;

import client.utils.ServerUtils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import com.google.inject.Inject;
import commons.*;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;


import java.net.URL;
import java.util.*;


public class BoardOverviewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private ObservableList<BoardList> data;

    private Board board;

    private boolean isDragging = false;

    @FXML
    private FlowPane mainBoard;

    @FXML
    private Label title;
    @FXML
    private Label copiedToClipboardMessage;

    @Inject
    public BoardOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /** This method is used for web sockets - we are registering for messages
     * In a way, subscribing to that location, every time the server sends something
     * which matches our patter, we get it
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        server.registerForMessages("/topic/lists", BoardList.class, list -> {
            Platform.runLater(() -> addListToBoard(list));
        });
        server.registerForMessages("/topic/lists/rename", BoardList.class, newList -> {
            Platform.runLater(() -> renameListById(newList.id, newList.title));
        });
        server.registerForMessages("/topic/lists", Integer.class, id -> {
            Platform.runLater(() -> deleteListById(id));
        });
        server.registerForMessages("/topic/cards", Card.class, card -> {
            Platform.runLater(() -> addCardToBoard(card));
        });
        server.registerForMessages("/topic/cards", Integer.class, id -> {
            Platform.runLater(() -> deleteCardById(id));
        });
        server.registerForMessages("/topic/cards/rename", Card.class, card -> {
            Platform.runLater(() -> renameCardById(card.id,card.title));
        });
        server.registerForMessages("/topic/boards/removed", Integer.class, id -> {
            Platform.runLater(() -> { if(id== board.id) back(); });
        });
        server.registerForMessages("/topic/boards/rename", Board.class, newBoard -> {
            Platform.runLater(() -> { if(board.id == newBoard.id) title.setText(newBoard.title); });
        });
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void saveBoardInDatabase(){
        this.board = server.addBoard(this.board);
    }

    public void assignToUser(User user){
        server.assignBoardToUser(user.id, this.board.id);
    }

    //region METHODS FOR BUTTONS

    public void addList() {
        mainCtrl.showAddList(board);
    }

    public void deleteBoard() {
        server.deleteBoard(board.id);
        mainCtrl.deleteBoard();
    }

    public void back() {
        mainCtrl.showWorkspace(mainCtrl.getUsername());
    }

    /**
     * Method that copies invite code to the clipboard
     */
    public void copyInvite(){
        StringSelection selection = new StringSelection(getInviteCode());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, null);
        copiedToClipboardMessage.setText("Copied to clipboard");

        // message gets deleted after 2 seconds
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> {
            copiedToClipboardMessage.setText("");
        });
        delay.play();
    }

    /**
     * Method that returns invite code to the user
     * separate private method due to security
     * @return
     */
    private String getInviteCode(){
        String selection = board.title+"#"+board.id;
        return selection;
    }

    //endregion

    //region METHODS FOR SOCKETS

    private void addListToBoard(BoardList list){
        try{
            FXMLLoader listLoader = new FXMLLoader(getClass().getResource("List.fxml"));
            Node listObject = listLoader.load();
            ListCtrl listObjectController = listLoader.getController();
            listObject.setUserData(listObjectController);
            assignListToController(listObjectController,list);
            mainBoard.getChildren().add(listObject);
            setDragReleaseList(listObject);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void deleteListById(int id){
        mainBoard.getChildren().stream()
                .filter(e -> ((ListCtrl)e.getUserData()).getListId()==id)
                .findFirst()
                .ifPresent(mainBoard.getChildren()::remove);
    }

    private void renameListById(int id,String title){
        mainBoard.getChildren().stream()
                .filter(e -> ((ListCtrl)e.getUserData()).getListId()==id)
                .findFirst()
                .ifPresent(e -> ((ListCtrl) e.getUserData()).setListTitleText(title));
    }

    private void addCardToBoard(Card card){
        mainBoard.getChildren().stream()
                .filter(e -> ((ListCtrl)e.getUserData()).getListId()==card.listId)
                .findFirst()
                .ifPresent(list -> {
                    try{
                        FXMLLoader cardLoader =
                                new FXMLLoader((getClass().getResource("Card.fxml")));
                        Node cardObject = cardLoader.load();
                        cardObject.setUserData(cardLoader.getController());
                        ((ListCtrl)list.getUserData()).getBoardList().cards.add(card);
                        assignAndAddCard(cardObject,card,(ListCtrl) list.getUserData());
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                });
    }

    private void deleteCardById(int id){
        for(Node n : mainBoard.getChildren())
            ((ListCtrl) n.getUserData()).getCardBox().getChildren().
                removeIf(e -> ((CardCtrl)e.getUserData()).getCardId()==id);
    }

    private void renameCardById(int id, String title){
        for(Node n : mainBoard.getChildren())
            for(Node c : ((ListCtrl) n.getUserData()).getCardBox().getChildren())
                if(((CardCtrl)c.getUserData()).getCardId()==id)
                    ((CardCtrl)c.getUserData()).setCardTitleText(title);
    }

    //endregion

    //region METHODS FOR REFRESH

    /** Assigns boardlist to controller and also sets controller
     * @param listObjectController the controller of the list node
     * @param currentList the list to be associated to the controller
     * @return the list controller with the associated boardList
     */
    private ListCtrl assignListToController(ListCtrl listObjectController, BoardList currentList){
        listObjectController.setBoardList(currentList);
        ///Attaching the boardList object to the listObjectController
        listObjectController.setListTitleText(currentList.title);
        //Setting the title of the list
        listObjectController.setServerAndCtrl(server,mainCtrl);
        //Setting the server and  ctrl because I have no idea how to inject it
        listObjectController.getListAddCardButton().
                setOnAction(event -> mainCtrl.showAddCard(currentList));
        //Telling the button what to do
        return listObjectController;
    }


    /** Method that instantiates and adds the card to the list
     * @param cardObject the card node (with associated ctrl)
     * @param currentCard the card to represent
     * @param listObjectController the controller for adding objects
     * @return the controller of the card
     * @throws IOException related to the loader
     */
    private CardCtrl assignAndAddCard(
            Node cardObject,
            Card currentCard,
            ListCtrl listObjectController){

        CardCtrl cardObjectController = (CardCtrl) cardObject.getUserData();
        //Getting the controller
        cardObjectController.setCard(currentCard);
        ///Attaching the card to be represented to the cardCtrl
        cardObjectController.setCardTitleText(currentCard.title);
        //Setting the title of the card
        cardObjectController.setServerAndCtrl(server,mainCtrl);
        //Just as done with lists

        listObjectController.addCardToList(cardObject);
        //Adding the card to the list
        addDragAndDrop(listObjectController.getAmountOfCardsInList(),
                (HBox) cardObject);
        //Setting drag and drop property

        return cardObjectController;
    }

    /**
     * Method for renaming upon double click
     */
    public void setHandlerTitle(){
        title.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                mainCtrl.showChangeTitle(this.board);
            }
        });
    }

    public void refresh() {
        //If we are dragging we don't want to recreate all cards
        if(isDragging) return;
        board = server.getBoardByID(board.id);
        title.setText(board.title);
        setHandlerTitle();
        try {
            mainBoard.getChildren().clear();
            var lists = board.lists;
            data = FXCollections.observableList(lists);
            for (BoardList currentList : data) {
                FXMLLoader listLoader = new FXMLLoader(getClass().getResource("List.fxml"));
                Node listObject = listLoader.load();
                ListCtrl listObjectController = listLoader.getController();
                listObject.setUserData(listLoader.getController());
                assignListToController(listObjectController,currentList);

                //Adding the cards to the list
                ObservableList<Card> cardsInList = FXCollections.observableList(currentList.cards);
                Collections.sort(cardsInList, (s1, s2) -> { return s1.index-s2.index; });
                currentList.setCards(cardsInList);
                for (Card currentCard : cardsInList) {
                    FXMLLoader cardLoader = new FXMLLoader((getClass().getResource("Card.fxml")));
                    Node cardObject = cardLoader.load();
                    cardObject.setUserData(cardLoader.getController());
                    assignAndAddCard(cardObject,currentCard,listObjectController);
                }

                mainBoard.getChildren().add(listObject);
                setDragReleaseList(listObject);
            }
        } catch (Exception e){
            e.printStackTrace();
            System.out.print(e.getMessage());
        }
    }

    //endregion

    //region DRAG AND DROP METHODS

    // method that activates snapshot image being available while dragging the card
    private void addPreview(final FlowPane board, final HBox card){
        ImageView imageView = new ImageView(card.snapshot(null, null));
        imageView.setManaged(false);
        imageView.setMouseTransparent(true);
        board.getChildren().add(imageView);
        board.setUserData(imageView);
        board.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                imageView.relocate(event.getX(), event.getY());
            }
        });
    }

    // method that stops showing preview when dragging is finished
    private void removePreview(final FlowPane board){
        isDragging = false;
        board.setOnMouseDragged(null);
        board.getChildren().remove(board.getUserData());
        board.setUserData(null);
    }
    // method that highlights the card when it is dragged and dropped
    private void setDragAndDropEffect(final HBox card){
        String initialStyle = card.getStyle();

        card.setOnMouseDragEntered(new EventHandler<MouseDragEvent>() {
            @Override
            public void handle(MouseDragEvent event) {
                card.setStyle("-fx-background-color: #ffffa0;");
            }
        });

        card.setOnMouseDragExited(new EventHandler<MouseDragEvent>() {
            @Override
            public void handle(MouseDragEvent event) {
                card.setStyle(initialStyle);
            }
        });

    }
    // method that sends information to the server about card changes
    private void adjustCards(int indexInitialList, int indexFinalList,
                             int indexCardDragged, int indexCardsDropped){
        if(indexCardDragged ==-1)
            return;
        // decided to leave the card as it was
        if(indexCardsDropped == -1 &&
                (indexInitialList == indexFinalList)){
            return;
        }

        BoardList initialList = ((ListCtrl)mainBoard.getChildren().
                get(indexInitialList).getUserData()).getBoardList();
        BoardList finalList = ((ListCtrl)mainBoard.getChildren().
                get(indexFinalList).getUserData()).getBoardList();
        Card card = initialList.getCardByIndex(indexCardDragged);

        server.deleteCard(card.getId());
        card.setIndex(indexCardsDropped);
        initialList.cards.remove(card);
        card.setList(indexInitialList==indexFinalList ?
                initialList:finalList);
        finalList.cards.add(server.addCard(card));
    }
    // method that handles the drag event on the list
    private void setDragReleaseList(Node list){
        list.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {
            @Override
            public void handle(MouseDragEvent event) {
                removePreview(mainBoard);

                Node initial = (Node) event.getGestureSource();
                Node initialCardsSection = initial.getParent();
                Node initialList = initialCardsSection.getParent();
                int indexOfInitialList = mainBoard.getChildren().indexOf(initialList);
                Node targetList = (Node) event.getSource();
                VBox targetCardsSection = (VBox) ((VBox)targetList ).getChildren().get(2);
                int indexOfList = mainBoard.getChildren().indexOf(targetList);

                HBox draggedCard = (HBox) initial;
                VBox draggedCardsSection = (VBox) initialCardsSection;
                int indexOfDraggingNode = draggedCardsSection.getChildren().indexOf(draggedCard);
                event.consume();
                if(indexOfList == indexOfInitialList)return;
                adjustCards(indexOfInitialList, indexOfList, indexOfDraggingNode,
                        targetCardsSection.getChildren().size());
            }
        });
        for(int i = 0;i<((VBox)list).getChildren().size();i++){
            if(i!=0){
                Node item = ((VBox)list).getChildren().get(i);
                item.setOnMouseDragReleased(event -> removePreview(mainBoard));
            }
            else{
                HBox item = (HBox)((VBox)list).getChildren().get(i);
                for(int j = 0;j<item.getChildren().size();j++){
                    Node itemChild = item.getChildren().get(j);
                    itemChild.setOnMouseDragReleased(event -> removePreview(mainBoard));
                }
            }
        }

    }
    // method that handles the drag event on the board
    private void setDragReleaseBoard(){
        mainBoard.setOnMouseDragReleased(event -> removePreview(mainBoard));
    }

    // sets drag and drop feature to cards
    private void addDragAndDrop(int cardNumber, final HBox card){
        //VBox cardsBox = (VBox) ((VBox)list).getChildren().get(2);
        card.setOnDragDetected(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event) {
                isDragging = true;
                card.startFullDrag();
                addPreview(mainBoard, card);
            }
        });
        // add style effects
        setDragAndDropEffect(card);
        card.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {
            @Override
            public void handle(MouseDragEvent event) {
                card.setStyle("");
                removePreview(mainBoard);
                //getting the index of the list that holds the card being dragged
                Node initial = (Node) event.getGestureSource();
                Node initialCardsSection = initial.getParent();
                Node initialList = initialCardsSection.getParent();
                int indexOfInitialList = mainBoard.getChildren().indexOf(initialList);
                // getting the index of the list that card is dropped on
                Node target = (Node) event.getSource();
                Node targetCardsSection = target.getParent();
                Node targetList = targetCardsSection.getParent();
                int indexOfList = mainBoard.getChildren().indexOf(targetList);

                HBox draggedCard = (HBox) initial;
                VBox draggedCardsSection = (VBox) initialCardsSection;

                VBox droppedCardsSection = (VBox) targetCardsSection;
                int indexOfDraggingNode = draggedCardsSection.getChildren().indexOf(draggedCard);
                int indexOfDropTarget = droppedCardsSection.getChildren().indexOf(target);
                if(indexOfDropTarget == -1){
                    if(indexOfList == indexOfInitialList)
                        indexOfDropTarget = Math.max(cardNumber-1,0);
                    else
                        indexOfDropTarget = droppedCardsSection.getChildren().size();
                }
                adjustCards(indexOfInitialList, indexOfList,
                        indexOfDraggingNode, indexOfDropTarget);
                event.consume();
            }
        });

        setDragReleaseBoard();

    }
    //endregion
}