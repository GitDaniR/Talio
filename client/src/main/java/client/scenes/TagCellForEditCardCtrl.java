package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.Tag;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import java.io.IOException;

public class TagCellForEditCardCtrl extends ListCell<Tag> {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private EditCardCtrl editCardCtrl;

    @Inject
    public TagCellForEditCardCtrl(ServerUtils server, MainCtrl mainCtrl, EditCardCtrl editCardCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.editCardCtrl = editCardCtrl;
    }

    @Override
    public void updateItem(Tag tagToSet, boolean empty){
        super.updateItem(tagToSet, empty);

        if(empty){
            setText(null);
            setGraphic(null);
        }else{
            Node tag = null;
            TagCtrl tagCtrl = null;
            try {
                FXMLLoader loader = new FXMLLoader(getClass()
                        .getResource("TagCellForEditCard.fxml"));
                tag = loader.load();
                tagCtrl = loader.getController();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // get the Checkbox element from the scene
            CheckBox check = (CheckBox)((AnchorPane)tag).getChildren().get(0);
            check.setSelected(tagRelatedToCard(tagToSet));

            // get the text element from the scene
            Text text = (Text) ((AnchorPane)tag).getChildren().get(1);
            text.setText(tagToSet.title);
            ((AnchorPane)tag).setStyle("-fx-background-color:"+tagToSet.color
                    .replace("0x", "#"));

            // set up the controller for the tag
            TagCtrl finalTagCtrl = tagCtrl;
            // set the server and mainController of the tag's controller
            finalTagCtrl.setServerAndCtrl(server, mainCtrl);
            // set the tag from controller to be the given tag
            finalTagCtrl.setTag(tagToSet);

            check.selectedProperty().addListener(observable -> {
                if(check.isSelected()){
                    editCardCtrl.addTag(tagToSet);
                    server.addCardToTag(tagToSet.id, editCardCtrl.getCardToEdit());

                }else{
                    editCardCtrl.removeTag(tagToSet);
                    server.removeCardFromTag(tagToSet.id, editCardCtrl.getCardToEdit());
                }
            });
            setText(null);
            setGraphic(tag);
        }
    }
    private boolean tagRelatedToCard(Tag tag1){
        Card card = server.getCardById(editCardCtrl.getCardToEdit().id);
        tag1 = server.getTagById(tag1.id);

        for(Tag t: card.tags){
            if(t.id == tag1.id) return true;
        }

        for(Card c: tag1.cards){
            if(c.id == card.id) return true;
        }

        return false;
    }
}
