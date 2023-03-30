package client.scenes;

import client.utils.ServerUtils;
import commons.Card;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TagCellForOverviewCtrl implements Initializable {

    @FXML
    private Label lblTagName;
    @FXML
    private HBox box;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    private MainCtrl mainCtrl;
    private ServerUtils server;

    private TagOverviewCtrl tagOverviewCtrl;
    private boolean userCanEdit;
    private Tag tag;

    public void setMainCtrlAndServer(MainCtrl mainCtrl, ServerUtils server,
                                     TagOverviewCtrl tagOverviewCtrl){
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.tagOverviewCtrl = tagOverviewCtrl;
        this.userCanEdit = true;
    }

    public void setMainCtrlAndServer(MainCtrl mainCtrl, ServerUtils server){
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.userCanEdit = false;
    }

    /**
     * This method associates the tag with the controller and sets
     * the style of the tag depending on where it is shown
     *
     * @param tagToBeSet tag to be represented within controller
     *
     */
    public void setTag(Tag tagToBeSet){
        this.tag = tagToBeSet;
        lblTagName.setText(tag.title);
        box.setStyle("-fx-background-color: " + tag.color.replace("0x", "#")
                +";" + " -fx-border-color: black");

        // change the style of the tag depending on use case
        setButtonVisibility();
    }

    public void editTag(){
        mainCtrl.showEditTag(tag);
    }

    public void deleteTag(){
        deleteFromCards();
        server.deleteTag(tag.id);
        this.tagOverviewCtrl.refresh();
    }

    private void setButtonVisibility(){
        if(userCanEdit){
            editButton.setVisible(true);
            deleteButton.setVisible(true);
        }
        else{
            editButton.setVisible(false);
            deleteButton.setVisible(false);
            box.setPrefWidth(160);
        }
    }

    private void deleteFromCards(){
        List<Card> cards = server.getAllCards();
        for(Card c: cards){
            server.detachTag(c.id, tag);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
