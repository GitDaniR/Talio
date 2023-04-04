/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

import commons.Board;
import jakarta.ws.rs.WebApplicationException;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import commons.BoardList;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class AddListCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private Board boardToAddTo;

    @FXML
    private TextField title;
    @FXML
    private Label errorLabel;

    @Inject
    public AddListCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * @param boardToAddTo setting the board to add to
     */
    public void setBoardToAddTo(Board boardToAddTo){

        this.boardToAddTo=boardToAddTo;
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

    public void addList() {
        if(getBoardList().title.equals("")){
            displayErrorText("List title cannot be empty!");
            return;
        }

        try {
            server.addBoardList(getBoardList());
        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        clearFields();
        mainCtrl.showBoard();
    }

    private BoardList getBoardList() {
        return new BoardList(title.getText(),boardToAddTo, boardToAddTo.id);
    }

    private void clearFields() {
        title.clear();
    }

    public void cancel() {
        clearFields();
        mainCtrl.showBoard();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainCtrl.consumeQuestionMarkTextField(title);
    }
}