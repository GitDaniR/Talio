package client.scenes;

import client.utils.ServerUtils;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import java.net.URL;
import java.util.ResourceBundle;

public class TagDisplayCtrl implements Initializable {

    @FXML
    private Label lblTagName;

    @FXML
    private HBox box;

    private MainCtrl mainCtrl;
    private ServerUtils server;

    private TagOverviewCtrl tagOverviewCtrl;
    private Tag tag;

    public void setMainCtrlAndServer(MainCtrl mainCtrl, ServerUtils server,
                                     TagOverviewCtrl tagOverviewCtrl){
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.tagOverviewCtrl = tagOverviewCtrl;
    }

    public void setTag(Tag tagToBeSet){
        this.tag = tagToBeSet;
        lblTagName.setText(tag.title);
        box.setStyle("-fx-background-color: " + tag.color.replace("0x", "#") +
                ";-fx-border-color: black;-fx-border-width: 2px;");
    }

    public void editTag(){
        mainCtrl.showEditTag(tag);
    }

    public void deleteTag(){
        server.deleteTag(tag.id);
        this.tagOverviewCtrl.refresh();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
