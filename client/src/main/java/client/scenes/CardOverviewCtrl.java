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

import java.net.URL;
import java.util.ResourceBundle;

import com.google.inject.Inject;

import client.utils.ServerUtils;
import commons.Quote;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

// I use this overview instead of QuoteOverview now while developing
// This will probably be changed when lists and board become available

public class CardOverviewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private ObservableList<Quote> data;
    @FXML
    private FlowPane flowPane;

    @Inject
    public CardOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        flowPane.setHgap(30);
        flowPane.setVgap(10);
    }

    // Heavily inspired by Una's experiments with adding lists
    // Probably will also be changed once lists become available
    // This is a quick "work-in-progress" version
    public void refresh() {
        flowPane.getChildren().clear();
        var cards = server.getCards();
        data = FXCollections.observableList(cards);

        for (Quote item : cards) {
            Pane pane = new Pane();

            Rectangle card = new Rectangle();
            card.setHeight(30);
            card.setWidth(150);
            card.setFill(Color.rgb(250, 150, 250));

            Label titleCard = new Label(item.getTitle());
            titleCard.setLabelFor(card);

            pane.getChildren().addAll(card, titleCard);
            flowPane.getChildren().add(pane);
        }
    }

    public void addCard(){
        mainCtrl.showCard();
    }

}
