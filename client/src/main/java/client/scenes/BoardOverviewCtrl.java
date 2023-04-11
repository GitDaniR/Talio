package client.scenes;

import client.utils.ServerUtils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import com.google.inject.Inject;
import commons.*;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.util.Duration;
import java.net.URL;
import java.util.*;
import java.util.List;

public class BoardOverviewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    public ScrollPane scene;
    @FXML
    public AnchorPane everything;
    @FXML
    private FlowPane mainBoard;
    @FXML
    private Label title;
    @FXML
    private Label copiedToClipboardMessage;
    @FXML
    private ImageView lockedLockView;
    @FXML
    private ImageView unlockedLockView;
    @FXML
    private ImageView crossedLockView;
    @FXML
    private SplitPane crossedControl;
    @FXML
    private Button lockedButton;
    @FXML
    private Button unlockedButton;
    @FXML
    private Tooltip lockedTooltip;
    @FXML
    private Tooltip unlockedTooltip;
    @FXML
    private Tooltip crossedTooltip;

    private ObservableList<BoardList> data;
    private Board board;

    private User userViewing;
    private boolean isDragging = false;
    private CardCtrl hoveredCardCtrl;
    private int cardHighlightX = -1;
    private int cardHighlightY = -1;
    private Timeline scrolltimeline = new Timeline();
    private double scrollVelocity = 0;
    private List<List<AnchorPane>> cardBoxes;
    private int speed = 50;
    private boolean isShiftPressed = false;

    private final KeyCombination shiftDownComb = new KeyCodeCombination(KeyCode.DOWN,
            KeyCombination.SHIFT_DOWN);
    private final KeyCombination shiftUpComb = new KeyCodeCombination(KeyCode.UP,
            KeyCombination.SHIFT_DOWN);

    @Inject
    public BoardOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupScrolling();
        addKeyboardShortcuts();
        setupImagesAndTooltips();
    }

    private void addKeyboardShortcuts(){
        everything.addEventHandler(KeyEvent.KEY_PRESSED, (EventHandler<KeyEvent>) keyEvent -> {
            switch(keyEvent.getCode()){
                case DELETE:
                case BACK_SPACE:
                    if(hoveredCardCtrl != null) hoveredCardCtrl.deleteCard();
                    break;
                case ENTER:
                    if(hoveredCardCtrl != null) hoveredCardCtrl.editCard();
                    break;
                case T:
                    if(hoveredCardCtrl != null) hoveredCardCtrl.quickAddTag();
                    break;
                case C:
                    if(hoveredCardCtrl != null) hoveredCardCtrl.quickAddPreset();
                    break;
                case E:
                    if(hoveredCardCtrl != null) hoveredCardCtrl.editTitle();
                    break;
                case SHIFT:
                    isShiftPressed = true;
                    break;
            }
        });
        everything.addEventFilter(KeyEvent.KEY_PRESSED,(EventHandler<KeyEvent>) keyEvent ->{
            switch (keyEvent.getCode()){
                case LEFT:
                    moveHighlight(-1, 0);
                    break;
                case RIGHT:
                    moveHighlight(1, 0);
                    break;
                case UP:
                    if(!isShiftPressed) moveHighlight(0, -1);
                    break;
                case DOWN:
                    if(!isShiftPressed) moveHighlight(0, 1);
                    break;
            }
        });
        everything.addEventFilter(KeyEvent.KEY_RELEASED,(EventHandler<KeyEvent>) keyEvent -> {
            if(shiftDownComb.match(keyEvent) && verifyWriteAccess())
                shiftHighlighted(1);
            else if(shiftUpComb.match(keyEvent) && verifyWriteAccess())
                shiftHighlighted(-1);
            else if(keyEvent.getCode()==KeyCode.SHIFT)
                isShiftPressed=false;
        });
    }

    private void setupImagesAndTooltips(){
        Duration preferredDelay = new Duration(300L);
        Image lockedLock = new Image("/client.images/lockedLock.png");
        Image unlockedLock = new Image("/client.images/unlockedLock.png");
        Image crossedLock = new Image("/client.images/crossedLock.png");
        lockedLockView.setImage(lockedLock);
        lockedTooltip.setShowDelay(preferredDelay);
        unlockedLockView.setImage(unlockedLock);
        unlockedTooltip.setShowDelay(preferredDelay);
        crossedLockView.setImage(crossedLock);
        crossedTooltip.setShowDelay(preferredDelay);
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setUserViewing(User userViewing){
        this.userViewing = userViewing;
    }

    public void saveBoardInDatabase(){
        this.board = server.addBoard(this.board);
        Preset resp = server.addPreset(new Preset("0xFFA500", "0x000000",
                new ArrayList<>(), "Drukas Original", this.board, this.board.id));
        server.setDefaultPreset(resp.id);
    }
    public void assignToUser(User user){
        server.assignBoardToUser(user.id, this.board.id);
    }
    private void setColors(){
        title.setTextFill(Paint.valueOf(board.colorBoardFont));
        mainBoard.setStyle("-fx-background-color: " +
                board.colorBoardBackground.replace("0x", "#"));
    }

    //region HIGHLIGHT METHODS

    private void incrementHoverHorizontally(int amount){
        cardHighlightX = Math.floorMod(cardHighlightX+amount,cardBoxes.size());
    }

    private void incrementHoverVertically(int amount){
        cardHighlightY = Math.floorMod(cardHighlightY+amount,cardBoxes.get(cardHighlightX).size());
    }

    /**
     * This method moves the highlight of the card in any direction
     * based on the values of x and y
     * @param x change of direction in x
     * @param y change of direction y
     */
    private void moveHighlight(int x, int y) {
        if(cardHighlightX != -1 && cardHighlightY != -1){
            setCardHighlight(cardBoxes.get(cardHighlightX).get(cardHighlightY),false);
        }
        if(cardHighlightX==-1 && cardHighlightY==-1){
            x=1;
            y=0;
            cardHighlightY=0;
            cardHighlightX=-x;
        }

        //if we are moving between lists
        if(x!=0){
            int iterations = 1;
            cardHighlightY=0;
            incrementHoverHorizontally(x);
            //while to find the first non-empty list
            while(iterations<cardBoxes.size()){
                if(cardBoxes.get(cardHighlightX).isEmpty()){
                    iterations++;
                    incrementHoverHorizontally(x);
                    //If list is empty we keep looking
                    //for next candidate to highlight
                } else break;
            }
            if(iterations>cardBoxes.size())
                return;//If no candidate was found
        } else
            incrementHoverVertically(y);

        setCardHighlight(cardBoxes.get(cardHighlightX).get(cardHighlightY),true);
        hoveredCardCtrl = (CardCtrl)cardBoxes.get(cardHighlightX).get(cardHighlightY).getUserData();
    }

    /**
     * This method shifts card up or down
     * @param dir the direction of the shifting
     */
    private void shiftHighlighted(int dir){
        //if no card is highlighted
        if(cardHighlightX==-1 || cardHighlightY==-1)
            return;
        Card highlightedCard = ((CardCtrl) cardBoxes.get(cardHighlightX).get(cardHighlightY)
                .getUserData()).getCard();
        server.updateCardList(highlightedCard,highlightedCard.listId,
                Math.floorMod(highlightedCard.index+dir,cardBoxes.get(cardHighlightX).size()));
        incrementHoverVertically(dir);
    }

    /**
     * Takes a card and fiinds its coords in the board
     * @param cardObject cardObject to fiind coords of
     */
    private void setCoordsOfCard(Node cardObject){
        for(int i=0;i<cardBoxes.size();i++)
            for(int j=0;j<cardBoxes.get(i).size();j++)
                if(cardBoxes.get(i).get(j).equals(cardObject)){
                    cardHighlightX = i;
                    cardHighlightY = j;
                    return;
                }
    }

    /**
     * This method sets the highlight property
     * @param cardObject cardObject to add highligh to
     */
    private void addHighlight(Node cardObject){
        cardObject.setOnMouseEntered(e->{

            if(hoveredCardCtrl == null || !cardBoxes.get(cardHighlightX)
                    .get(cardHighlightY).equals(cardObject)) {
                cardObject.requestFocus();
                if (cardHighlightX != -1 && cardHighlightY != -1)
                    setCardHighlight(cardBoxes.get(cardHighlightX).get(cardHighlightY), false);
                //disabling previous highlight
                setCardHighlight(cardObject, true);
                hoveredCardCtrl = (CardCtrl) cardObject.getUserData();
                setCoordsOfCard(cardObject);
            }
        });
        cardObject.setOnMouseExited(e->{
            setCardHighlight(cardObject,false);
            //if(cardBoxes.get(cardHighlightX).get(cardHighlightY).equals(cardObject)){
            hoveredCardCtrl = null;
            cardHighlightX = -1;
            cardHighlightY = -1;
            //}
        });
    }

    //endregion

    //region METHODS FOR BUTTONS

    public void addList() {
        if(!verifyWriteAccess())
            showWriteAccessAlert();
        else
            mainCtrl.showAddList(board);
    }

    public void setPassword(){
        mainCtrl.showSetPassword(userViewing,board);
    }

    public void enterPassword(){
        mainCtrl.showEnterPassword(userViewing,board);
    }

    public void showTags(){
        if(!verifyWriteAccess())
            showWriteAccessAlert();
        else
            mainCtrl.showTagOverview(this.board);
    }

    public void showCustomization(){
        if(!verifyWriteAccess())
            showWriteAccessAlert();
        else
            mainCtrl.showCustomization(this.board);
    }

    public void deleteBoard() {
        if(!verifyWriteAccess())
            showWriteAccessAlert();
        else{
            server.deleteBoard(board.id);
            mainCtrl.deleteBoard();
        }
    }

    public void back() {
        mainCtrl.showWorkspace(mainCtrl.getUsername());
    }

    public void underlineText(){
        title.setUnderline(true);
    }
    public void undoUnderline(){
        title.setUnderline(false);
    }

    /**
     * Method that copies invite code to the clipboard
     */
    public void copyInvite(){
        StringSelection selection = new StringSelection(getInviteCode());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, null);
        copiedToClipboardMessage.setText("Copied invite code to clipboard: "+getInviteCode());

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

    //region PASSWORD METHODS

    /**
     * @return true if user has write access
     */
    private boolean verifyWriteAccess(){
        return mainCtrl.getIsAdmin() || board.password.equals("") ||
                board.password.equals("NO_PASSWORD") ||
                userViewing.unlockedBoards.stream().map(e -> e.id).anyMatch(e-> e== board.id);
    }

    private void showWriteAccessAlert(){
        var alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setContentText("You don't have write access!");
        alert.showAndWait();
    }

    private void setPasswordRights(){
        //if there is no password we don't care
        if(board.password.equals("") || board.password.equals("NO_PASSWORD")) {
            crossedLockView.setVisible(true);
            crossedTooltip.setText("The board doesn't have any password");
            unlockedButton.setDisable(true);
            unlockedTooltip.setText("The board is already unlocked");
            lockedButton.setDisable(false);
            lockedTooltip.setText("Set the password of the board");
        } else {
            //if there is a password, we need to check if we have access
            crossedLockView.setVisible(false);
            //if write access
            if(userViewing.unlockedBoards.contains(board) || mainCtrl.getIsAdmin()){
                unlockedButton.setDisable(true);
                unlockedTooltip.setText("You already inputted the password!");
                lockedButton.setDisable(false);
                lockedTooltip.setText("Change the password of the board");
            } else {
                unlockedButton.setDisable(false);
                unlockedTooltip.setText("Press to input the password of the board" +
                        " and gain write access");
                lockedButton.setDisable(true);
                lockedTooltip.setText("You don't have access to change the password!");
            }
        }
    }

    //endregion

    //region METHODS FOR SOCKETS

    public void subscribeToSocketsBoardOverview(){
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
        server.registerForMessages("/topic/cards/edit", Card.class, card -> {
            Platform.runLater(()->{
                refresh();
                if(cardHighlightX!=-1 && cardHighlightY!=-1)
                    setCardHighlight(cardBoxes.get(cardHighlightX).get(cardHighlightY),true);
            });
        });
        server.registerForMessages("/topic/boards/removed", Integer.class, id -> {
            Platform.runLater(() -> { if(board==null || id==board.id) back(); });
        });
        server.registerForMessages("/topic/boards/rename", Board.class, newBoard -> {
            Platform.runLater(() -> { if(board !=null && board.id == newBoard.id)
                    title.setText(newBoard.title); });
        });
        //Basically I just need to update the card
        server.registerForMessages("/topic/subtasks", Integer.class, id -> {
            Platform.runLater(() -> renameCardById(id, ""));
        });
        server.registerForMessages("/topic/boards/colors", Double.class, dummy ->{
            Platform.runLater(this::refresh);
        });
    }

    private void addListToBoard(BoardList list){
        try{
            FXMLLoader listLoader = new FXMLLoader(getClass().getResource("List.fxml"));
            Node listObject = listLoader.load();
            ListCtrl listObjectController = listLoader.getController();
            listObject.setUserData(listObjectController);
            assignListToController(listObjectController,list);
            mainBoard.getChildren().add(listObject);
            setDragReleaseList(listObject);
            refresh();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void deleteListById(int id){
        mainBoard.getChildren().stream()
                .filter(e -> ((ListCtrl)e.getUserData()).getListId()==id)
                .findFirst()
                .ifPresent(mainBoard.getChildren()::remove);
        refresh();
    }

    private void renameListById(int id,String title){
        mainBoard.getChildren().stream()
                .filter(e -> ((ListCtrl)e.getUserData()).getListId()==id)
                .findFirst()
                .ifPresent(e -> ((ListCtrl) e.getUserData()).setListTitleText(title));
        refresh();
    }

    private void addCardToBoard(Card card){
        mainBoard.getChildren().stream()
                .filter(e -> ((ListCtrl)e.getUserData()).getListId()==card.listId)
                .findFirst()
                .ifPresent(list -> {
                    try{
                        FXMLLoader cardLoader =
                                new FXMLLoader((getClass().getResource("CardNew.fxml")));
                        Node cardObject = cardLoader.load();
                        cardObject.setUserData(cardLoader.getController());
                        ((ListCtrl)list.getUserData()).getBoardList().cards.add(card);
                        assignAndAddCard(cardObject,card,(ListCtrl) list.getUserData());
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                });
        refresh();
    }

    private void deleteCardById(int id){
        for(Node n : mainBoard.getChildren())
            ((ListCtrl) n.getUserData()).getCardBox().getChildren().
                removeIf(e -> ((CardCtrl)e.getUserData()).getCard().id==id);
        refresh();
    }

    private PauseTransition inputDelay;
    private boolean isPlaying = false;


    private void renameCardById(int id, String title){
        if(inputDelay==null){
            inputDelay = new PauseTransition(Duration.seconds(0.2));
            inputDelay.setOnFinished(event -> {
                isPlaying = false;
                refresh();
                if(cardHighlightX!=-1 && cardHighlightY!=-1)
                    setCardHighlight(cardBoxes.get(cardHighlightX).get(cardHighlightY),true);
                System.out.println("Stopped");
            });
            isPlaying=false;
        }
        if(!isPlaying){
            isPlaying = true;
            inputDelay.play();
        }
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
                setOnAction(event -> listObjectController.addDefaultCard());
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
        //Setting the card to be represented and also changing the values accordingly
        cardObjectController.setServerAndCtrl(server,mainCtrl);
        cardObjectController.setCardAndAttributes(currentCard);
        //Just as done with lists

        //if card is double-clicked editCard scene is shown
        cardObject.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                cardObjectController.editCard();
            }
        });

        if(verifyWriteAccess())
            addDragAndDrop(listObjectController.getAmountOfCardsInList(), (AnchorPane) cardObject);
        //Setting drag and drop property

        addHighlight(cardObject);
        //setting Highlight

        listObjectController.addCardToList(cardObject);
        //Adding the card to the list

        return cardObjectController;
    }

    private void setCardHighlight(Node card,boolean value) {
        if (value)
            card.setStyle("-fx-border-color: black; -fx-border-width: 4; " +
                    "-fx-background-color: rgba(217, 192, 31, 0.49);" +
                    " -fx-border-radius: 5 5 5 5; -fx-background-radius: 5 5 5 5;");
        else {
            CardCtrl cardCtrl = (CardCtrl) card.getUserData();
            Preset preset = server.getPresetById(cardCtrl.getCard().presetId);

            card.setStyle("-fx-border-color: black; -fx-border-width: 4; " +
                    "-fx-background-color: " + preset.backgroundColor.replace("0x", "#") +
                    "; -fx-border-radius: 5 5 5 5; -fx-background-radius: 5 5 5 5;");
        }
    }

    /**
     * Method for renaming upon double click
     */
    public void setHandlerTitle(){
        title.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                if(!verifyWriteAccess())
                    showWriteAccessAlert();
                else
                    mainCtrl.showChangeTitle(this.board);
            }
        });
    }

    public void refresh() {
        if(board == null) return;
        cardBoxes = new ArrayList<>();
        board = server.getBoardByID(board.id);
        userViewing = server.getUserById(userViewing.id);
        title.setText(board.title);
        setPasswordRights();
        setColors();
        setHandlerTitle();
        try {
            if(mainBoard.getChildren().size()>0)
                mainBoard.getChildren().clear();
            var lists = board.lists;
            data = FXCollections.observableList(lists);
            for (BoardList currentList : data) {
                FXMLLoader listLoader = new FXMLLoader(getClass().getResource("List.fxml"));
                Node listObject = listLoader.load();
                ListCtrl listObjectController = listLoader.getController();
                listObject.setUserData(listLoader.getController());
                assignListToController(listObjectController,currentList);

                cardBoxes.add(new ArrayList<>());

                listObjectController.setServerAndCtrl(server, mainCtrl);
                listObjectController.setFontColor();
                // customization of lists
                listObject.setStyle("-fx-background-color:"+
                        board.colorListsBackground.replace("0x", "#") +
                        "; -fx-border-color: black; -fx-border-width: 1");
                //Adding the cards to the list
                ObservableList<Card> cardsInList = FXCollections.observableList(currentList.cards);
                Collections.sort(cardsInList, (s1, s2) -> { return s1.index-s2.index; });
                currentList.setCards(cardsInList);
                for (Card currentCard : cardsInList) {
                    FXMLLoader cardLoader = new FXMLLoader((getClass()
                            .getResource("CardNew.fxml")));
                    Node cardObject = cardLoader.load();
                    cardObject.setUserData(cardLoader.getController());
                    assignAndAddCard(cardObject,currentCard,listObjectController);
                    cardBoxes.get(cardBoxes.size()-1).add((AnchorPane) cardObject);
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

    /**
     * Method that sets up the animation
     * for scrolling while drag and dropping the card
     */
    private void setupScrolling() {
        scrolltimeline.setCycleCount(Timeline.INDEFINITE);
        scrolltimeline.getKeyFrames().add(new KeyFrame(Duration.millis(20),
                (actionEvent) -> { dragScroll();}));
        scene.setOnMouseDragExited(event -> {
            if (event.getY() > 0) {
                scrollVelocity = 1.0 / speed;
            }
            else {
                scrollVelocity = -1.0 / speed;
            }
            if (isDragging){
                scrolltimeline.stop();
                scrolltimeline.play();
            }else{
                scrolltimeline.stop();
                removePreview(mainBoard);
            }
        });

        scene.setOnMouseReleased(event -> {
            removePreview(mainBoard);
            scrolltimeline.stop();
        });
        scene.setOnMouseDragEntered(event -> {
            scrolltimeline.stop();
            isDragging = true;
        });

        scene.setOnMouseDragOver(event -> {
            scrolltimeline.stop();
        });

        scene.setOnMouseDragReleased(event->{
            scrolltimeline.stop();
            removePreview(mainBoard);
            isDragging = false;
        });

        scene.setOnScroll((ScrollEvent event)-> {
            scrolltimeline.stop();
        });


    }

    /**
     * Method that returns the scrollbar to be
     * scrolled the right amount
     */
    private void dragScroll() {
        ScrollBar sb = getVerticalScrollbar();
        if (sb != null) {
            double newValue = sb.getValue() + scrollVelocity;
            newValue = Math.min(newValue, 1.0);
            newValue = Math.max(newValue, 0.0);
            sb.setValue(newValue);
        }
    }

    /**
     * Method that returns the scrollbar for the scrolling
     * animation
     * @return
     */
    private ScrollBar getVerticalScrollbar() {
        ScrollBar result = null;
        for (Node n : scene.lookupAll(".scroll-bar")) {
            if (n instanceof ScrollBar) {
                ScrollBar bar = (ScrollBar) n;
                if (bar.getOrientation().equals(Orientation.VERTICAL)) {
                    result = bar;
                }
            }
        }
        return result;
    }


    /**
     * Method that makes the snapshot of card to be visible
     * while drag&dropping the card
     * @param board
     * @param card
     */
    private void addPreview(final FlowPane board, final AnchorPane card){
        ImageView imageView = new ImageView(card.snapshot(null, null));
        imageView.setManaged(false);
        imageView.setMouseTransparent(true);
        imageView.setVisible(false);
        board.getChildren().add(imageView);
        board.setUserData(imageView);
        board.setOnMouseDragOver(event -> {
            imageView.setVisible(true);
            imageView.relocate(event.getX(), event.getY());
        });
    }

    /**
     * Method that removes the snapsshot of the card
     * when the card is dropped
     * @param board - board containing the cards
     */
    private void removePreview(final FlowPane board){
        isDragging = false;
        board.setOnMouseDragged(null);
        board.getChildren().remove(board.getUserData());
        board.setUserData(null);
    }


    /**
     * Method that highlights the card when its dragging starts,
     * @param card - card to be highlighted
     */
    private void setDragAndDropEffect(final AnchorPane card){
        String initialStyle = card.getStyle();
        card.setOnMouseDragEntered(new EventHandler<MouseDragEvent>() {
            @Override
            public void handle(MouseDragEvent event) {
                card.setStyle("-fx-border-color: #ffffa0;" +
                    " -fx-border-width: 4; -fx-background-color: #ffffa0;");
            }
        });

        card.setOnMouseDragExited(event -> {card.setStyle(initialStyle);});
    }

    /**
     * Method that adjusts the card according to the new index
     * set up by the user's
     * @param indexInitialList - index of the list that the card
     *                         was being dragged of
     * @param indexFinalList - index of the list on which the card
     *                       was dropped
     * @param indexCardDragged - index of the card inside the initial list
     * @param indexCardsDropped - index on which card should be dropped
     */
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

        server.updateCardList(card, finalList.getId(), indexCardsDropped);
        refresh();
    }

    /**
     * Method to get the list from a node that was gotten from a gesture
     * @param root the node of the gesture
     * @return
     */
    private Node getListFromGestureNode(Node root){
        Node cardSelection = root.getParent();
        Node scrollPane = cardSelection.getParent();
        Node scrollPaneSkin = scrollPane.getParent().getParent();
        return scrollPaneSkin.getParent();
    }

    /**
     * Method that handles the event when the card
     * is dropped on a list and not on a specific card
     * @param list - list on which card was dropped
     */
    private void setDragReleaseList(Node list){
        list.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {
            @Override
            public void handle(MouseDragEvent event) {
                removePreview(mainBoard);
                double eventY = event.getY();
                Node initial = (Node) event.getGestureSource();
                Node initialCardsSection = initial.getParent();
                Node initialList = getListFromGestureNode(initial);
                int indexOfInitialList = mainBoard.getChildren().indexOf(initialList);
                Node targetList = (Node) event.getSource();
                ScrollPane targetScrollPane = (ScrollPane) ((VBox)targetList).getChildren().get(2);
                VBox targetCardsSection = (VBox) targetScrollPane.getContent();
                int indexOfList = mainBoard.getChildren().indexOf(targetList);
                AnchorPane draggedCard = (AnchorPane) initial;
                VBox draggedCardsSection = (VBox) initialCardsSection;
                int indexOfDraggingNode = draggedCardsSection.getChildren().indexOf(draggedCard);
                event.consume();
                if(indexOfList == indexOfInitialList) {
                    Bounds  parentBounds = targetScrollPane.getBoundsInParent();
                    double startY = parentBounds.getMinY();
                    if(eventY<startY){
                        adjustCards(indexOfInitialList, indexOfList, indexOfDraggingNode, 0);
                    }else{
                        adjustCards(indexOfInitialList, indexOfList, indexOfDraggingNode,
                                targetCardsSection.getChildren().size()-1);}
                }else{
                    adjustCards(indexOfInitialList, indexOfList, indexOfDraggingNode,
                            targetCardsSection.getChildren().size());
                }
            }
        });
        for(int i = 0;i<((VBox)list).getChildren().size();i++){
            if(i!=0 && i!=2){
                Node item = ((VBox)list).getChildren().get(i);
                item.setOnMouseDragReleased(event -> removePreview(mainBoard));
            }
            else if(i==0){
                HBox item = (HBox)((VBox)list).getChildren().get(i);
                for(int j = 0;j<item.getChildren().size();j++){
                    Node itemChild = item.getChildren().get(j);
                    itemChild.setOnMouseDragReleased(event -> removePreview(mainBoard));
                }
            }
        }
    }

    /**
     * Method that handles the event when card is dropped on a board
     * and not inside the list
     */
    private void setDragReleaseBoard(){
        mainBoard.setOnMouseDragReleased(event -> {
            removePreview(mainBoard);
        });
    }

    /**
     * Method that sets support for drag and dropping tha card
     * @param cardNumber -  index of the card
     * @param card - card to add drag&drop for
     */
    private void addDragAndDrop(int cardNumber, final AnchorPane card){
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
                Node initialList = getListFromGestureNode(initial);
                int indexOfInitialList = mainBoard.getChildren().indexOf(initialList);
                // getting the index of the list that card is dropped on
                Node target = (Node) event.getSource();
                Node targetCardsSection = target.getParent();
                Node targetList = getListFromGestureNode(target);
                int indexOfList = mainBoard.getChildren().indexOf(targetList);

                VBox draggedCardsSection = (VBox) initialCardsSection;

                VBox droppedCardsSection = (VBox) targetCardsSection;
                int indexOfDraggingNode = draggedCardsSection.getChildren().indexOf(initial);
                int indexOfDropTarget = droppedCardsSection.getChildren().indexOf(target);
                if(indexOfDropTarget == -1){
                    if(indexOfList == indexOfInitialList)
                        indexOfDropTarget = Math.max(indexOfDraggingNode,0);
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