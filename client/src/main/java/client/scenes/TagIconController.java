package client.scenes;

import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class TagIconController implements Initializable {
    @FXML
    private Text txtLetter;
    @FXML
    private Ellipse background;
    private Tag tag;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setTag(Tag tag){
        this.tag = tag;
        if(!tag.title.isEmpty()){
            txtLetter.setText(String.valueOf(tag.title.charAt(0)));
        }
        background.setFill(Paint.valueOf(tag.colorBackground));
        txtLetter.setFill(Paint.valueOf(tag.colorFont));
    }
}
