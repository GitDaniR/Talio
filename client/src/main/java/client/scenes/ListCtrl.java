package client.scenes;

import client.utils.ServerUtils;
import commons.BoardList;
import commons.Card;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;


import java.net.URL;
import java.util.ResourceBundle;

public class ListCtrl extends AnchorPane implements Initializable{
    @FXML
    private Label listTitle;
    @FXML
    private VBox cardBox;
    @FXML
    private Button listAddCard;
    @FXML
    private Button listCloseButton;
    @FXML
    private Button listEditButton;

    private BoardList boardList;

    private ServerUtils server;
    private MainCtrl mainCtrl;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void addCardToList(Node card){
        cardBox.getChildren().add(card);
    }

    public void addDefaultCard(){
        try {
            server.addCard(new Card("Task", "", boardList.cards.size(), boardList, boardList.id));
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        mainCtrl.showBoard();
    }

    public void deleteList(){
        server.deleteBoardList(boardList.id);
        mainCtrl.refreshBoardOverview();
    }

    public void editList(){
        mainCtrl.showEditList(boardList);
    }

    /** Sets the text of the title of the list
     * @param text
     */
    public void setListTitleText(String text) {
        listTitle.setText(text);
    }

    public void setServerAndCtrl(ServerUtils server,MainCtrl mainCtrl){
        this.server=server;
        this.mainCtrl=mainCtrl;
    }

    /** This method associates a boardList to the controller for easy access
     * @param boardList the boardList to be associated with the controller
     */
    public void setBoardList(BoardList boardList){
        this.boardList = boardList;
    }

    public VBox getCardBox() {
        return cardBox;
    }

    public int getAmountOfCardsInList(){
        return boardList.cards.size();
    }

    /**
     * @return the list button of the list
     */
    public Button getListAddCardButton() {
        return listAddCard;
    }
}
