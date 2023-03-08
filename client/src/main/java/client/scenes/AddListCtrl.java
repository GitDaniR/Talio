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

import client.utils.FakeServerUtils;
import com.google.inject.Inject;

import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import commons.BoardList;

public class AddListCtrl {

    private final FakeServerUtils fakeServer;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField title;

    @Inject
    public AddListCtrl(FakeServerUtils fakeServer, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.fakeServer = fakeServer;
    }

    public void add() {
        try {
            fakeServer.addBoardList(getBoardList());
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
        return new BoardList(title.getText());
    }

    private void clearFields() {
        title.clear();
    }

    public void cancel() {
        clearFields();
        mainCtrl.showBoard();
    }
}