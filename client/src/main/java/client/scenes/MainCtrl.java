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

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

// This MainCtrl is modified to use the CardOverview and AddCard that I created
// Thus it will change once lists and board become available
public class MainCtrl {

    private Stage primaryStage;
    private Scene addCard;
    private AddCardCtrl addCardCtrl;
    private CardOverviewCtrl cardOverviewCtrl;
    private Scene cardOverview;

    private BoardOverviewCtrl boardOverviewCtrl;
    private Scene board;

    private AddListCtrl addCtrl;
    private Scene add;


    public void initialize(Stage primaryStage,
            Pair<AddCardCtrl, Parent> addCard,
                           Pair<CardOverviewCtrl, Parent> cardOverview,
                           Pair<AddListCtrl, Parent> add,
                           Pair<BoardOverviewCtrl, Parent> board) {
        this.primaryStage = primaryStage;

        this.boardOverviewCtrl = board.getKey();
        this.board = new Scene(board.getValue());

        this.addCtrl = add.getKey();
        this.add = new Scene(add.getValue());

        this.addCardCtrl = addCard.getKey();
        this.addCard = new Scene(addCard.getValue());
        this.cardOverviewCtrl = cardOverview.getKey();
        this.cardOverview = new Scene(cardOverview.getValue());

        showBoard();
        primaryStage.show();
    }

    public void showCardOverview() {
        primaryStage.setTitle("Cards: Overview");
        primaryStage.setScene(cardOverview);
        cardOverviewCtrl.refresh();
    }

    public void showCard() {
        primaryStage.setTitle("A new card");
        primaryStage.setScene(addCard);
        addCard.setOnKeyPressed(e -> addCardCtrl.keyPressed(e));
    }

    public void showBoard() {
        primaryStage.setTitle("Board Overview");
        primaryStage.setScene(board);
    }

    public void showAdd() {
        primaryStage.setTitle("Adding List");
        primaryStage.setScene(add);
    }
}