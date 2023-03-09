package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;


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

    private Integer listID;

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    ///This probably doesn't inject anything - needs to be solved!
    @Inject
    public ListCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void addCardToList(Node card){
        cardBox.getChildren().add(card);
    }

    public void deleteList(){
        server.deleteBoardList(listID);
        mainCtrl.deleteList();
    }

    /** Sets the text of the title of the list
     * @param text
     */
    public void setListTitleText(String text) {
        listTitle.setText(text);
    }

    public void setListID(int listID){
        this.listID = listID;
    }

    /**
     * @return the list button of the list
     */
    public Button getListAddCardButton() {
        return listAddCard;
    }
}
