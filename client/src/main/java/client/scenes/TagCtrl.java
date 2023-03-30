package client.scenes;

import client.utils.ServerUtils;
import commons.Card;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.ResourceBundle;

public class TagCtrl implements Initializable {

    private  ServerUtils server;
    private  MainCtrl mainCtrl;

    @FXML
    private Text title;
    @FXML
    private CheckBox check;

    private Tag tag;

    public void setServerAndCtrl(ServerUtils server, MainCtrl mainCtrl){
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void addTag(Card card){
        tag.addToCard(card);
    }

    public void removeTag(Card card){
        tag.removeFromCard(card);
    }

    public void setTag(Tag tag){
        this.tag = tag;
    }

}
