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

import commons.Board;
import commons.BoardList;
import commons.Card;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.Timer;

public class MainCtrl {

    private Stage primaryStage;
    private Scene addCard;
    private AddCardCtrl addCardCtrl;

    private BoardOverviewCtrl boardOverviewCtrl;
    private Scene board;

    private AddListCtrl addListCtrl;
    private Scene addList;

    private WelcomePageCtrl welcomePageCtrl;
    private Scene welcomePage;

    private EditListCtrl editListCtrl;
    private Scene editList;

    private EditCardCtrl editCardCtrl;
    private Scene editCard;

    private double windowHeight;
    private double windowWidth;

    //Maintain the current running timer so se can stop it when changing views/exiting the app
    private Timer currentTimer;

    //a const to easily manage the refresh rate of auto-sync
    //buggy when below 200
    public static final int REFRESH_RATE = 200;


    public void initialize(Stage primaryStage,
            Pair<AddCardCtrl, Parent> addCard,
                           Pair<AddListCtrl, Parent> addList,
                           Pair<BoardOverviewCtrl, Parent> board,
                           Pair<WelcomePageCtrl, Parent> welcomePage,
                           Pair<EditListCtrl, Parent> editList,
                           Pair<EditCardCtrl, Parent> editCard) {
        this.primaryStage = primaryStage;

        this.boardOverviewCtrl = board.getKey();
        this.board = new Scene(board.getValue());

        this.addListCtrl = addList.getKey();
        this.addList = new Scene(addList.getValue());

        this.addCardCtrl = addCard.getKey();
        this.addCard = new Scene(addCard.getValue());

        this.welcomePageCtrl = welcomePage.getKey();
        this.welcomePage = new Scene(welcomePage.getValue());

        this.editListCtrl=editList.getKey();
        this.editList = new Scene(editList.getValue());

        this.editCardCtrl = editCard.getKey();
        this.editCard = new Scene(editCard.getValue());

        showWelcomePage();
        primaryStage.show();
    }

    private void storeWindowSize(Scene current){
        windowHeight = current.getHeight();
        windowWidth = current.getWidth();
    }

    private void setWindowSize(){
        primaryStage.setHeight(windowHeight);
        primaryStage.setWidth(windowWidth);
    }

    public void showAddCard(BoardList list) {
        primaryStage.setTitle("A new card");
        addCardCtrl.setList(list);
        primaryStage.setScene(addCard);
        cancelTimer();
    }

    public void showBoard() {
        //storeWindowSize(primaryStage.getScene());

        primaryStage.setTitle("Board Overview");
        primaryStage.setScene(board);
        boardOverviewCtrl.refresh();
        cancelTimer();
        currentTimer = boardOverviewCtrl.startTimer(REFRESH_RATE);
    }

    public void showAddList(Board boardToAddTo) {
        primaryStage.setTitle("Adding List");
        primaryStage.setScene(addList);
        addListCtrl.setBoardToAddTo(boardToAddTo);
        cancelTimer();
    }

    public void showEditList(BoardList boardListToEdit){
        primaryStage.setTitle("Editing List");
        primaryStage.setScene(editList);
        editListCtrl.setBoardListToEdit(boardListToEdit);
        cancelTimer();
    }


    public void showWelcomePage() {
        primaryStage.setTitle("Welcome Page");
        primaryStage.setScene(welcomePage);
        cancelTimer();
    }

    public void refreshBoardOverview(){
        boardOverviewCtrl.refresh();
    }

    //Method to cancel any timer currently running
    //We should cancel the timer any time we switch views
    public void cancelTimer(){
        if(currentTimer == null) return;
        currentTimer.cancel();
    }

    public void deleteCard() {
        boardOverviewCtrl.refresh();
    }

    public void showEditCard(Card cardToEdit) {
        primaryStage.setTitle("Editing Card");
        primaryStage.setScene(editCard);
        editCardCtrl.setCardToEdit(cardToEdit);
    }
}