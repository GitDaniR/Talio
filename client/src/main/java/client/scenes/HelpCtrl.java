package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.text.Text;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class HelpCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private Text textInfo;
    @FXML
    private Text shortcutGuide;
    private Scene previousScene;
    private String previousTitle;

    @Inject
    public HelpCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File f = new File(this.getClass().getResource("/helpPageText.txt").getPath());
        try{
            Scanner s = new Scanner(f);
            s.useDelimiter("/end");
            textInfo.setText(s.next());
            shortcutGuide.setText(s.next());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void back(){
        mainCtrl.backFromHelp();
    }

    public Scene getPreviousScene() {
        return previousScene;
    }

    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    public String getPreviousTitle() {
        return previousTitle;
    }

    public void setPreviousTitle(String previousTitle) {
        this.previousTitle = previousTitle;
    }
}
