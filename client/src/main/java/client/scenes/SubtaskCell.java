package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Subtask;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class SubtaskCell extends ListCell<Subtask> {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @Inject
    public SubtaskCell(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void updateItem(Subtask task, boolean empty){
        super.updateItem(task, empty);

        if(empty){
            setText(null);
            setGraphic(null);
        }else{
            Node subtask = null;
            SubtaskCtrl subtaskCtrl = null;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("SubtaskCell.fxml"));
                subtask = loader.load();
                subtaskCtrl = loader.getController();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // get the Checkbox element from the scene
            CheckBox check = (CheckBox)((AnchorPane)subtask).getChildren().get(0);
            // get the text element from the scene
            Text text = (Text) ((AnchorPane)subtask).getChildren().get(1);
            // set up the controller for the subtask
            SubtaskCtrl finalSubtaskCtrl = subtaskCtrl;
            // set the server and mainController of the subtask's controller
            finalSubtaskCtrl.setServerAndCtrl(server, mainCtrl);
            // set the subtask from controller to be the given task
            finalSubtaskCtrl.setSubtask(task);

            check.selectedProperty().addListener(observable -> {
                if(check.isSelected()){
                    text.setStyle("-fx-strikethrough: true");
                    finalSubtaskCtrl.finishTask();

                }else{
                    text.setStyle("-fx-strikethrough: false");
                    finalSubtaskCtrl.unfinishTask();
                }
            });
            setText(null);
            setGraphic(subtask);


        }

    }
}
