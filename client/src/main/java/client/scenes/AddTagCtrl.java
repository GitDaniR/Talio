package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Tag;
import jakarta.ws.rs.WebApplicationException;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.util.Duration;
import java.net.URL;
import java.util.ResourceBundle;

public class AddTagCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private TextField title;
    @FXML
    private ColorPicker colorPickerBackground;
    @FXML
    private ColorPicker colorPickerFont;
    @FXML
    private Label errorLabel;
    private Board boardToAddTo;

    @Inject
    public AddTagCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void cancel(){
        clearFields();
        mainCtrl.showTagOverview(this.boardToAddTo);
    }

    private void clearFields() {
        title.clear();
        colorPickerBackground.setValue(Color.valueOf("0xffffff"));
        colorPickerFont.setValue(Color.valueOf("0xffffff"));
    }

    private PauseTransition delay;
    public void displayErrorText(String text){
        errorLabel.setText(text);
        // message gets deleted after 2 seconds
        if(delay!=null)
            delay.stop();//stop previous delay
        delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> {
            errorLabel.setText("");
        });
        delay.play();
    }

    public void ok() {
        if(title.getText().equals("")){
            displayErrorText("Tag title cannot be empty!");
            return;
        }
        try {
            server.addTag(getTag());

        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        clearFields();
        mainCtrl.showTagOverview(boardToAddTo);
    }

    private Tag getTag() {
        var t = title.getText();
        var cBackground = colorPickerBackground.getValue().toString();
        var cFont = colorPickerFont.getValue().toString();

        return new Tag(t, cBackground, cFont, boardToAddTo, boardToAddTo.id);
    }

    public void setBoard(Board board){
        this.boardToAddTo = board;
    }
}
