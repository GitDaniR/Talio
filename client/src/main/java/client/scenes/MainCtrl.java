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

import java.util.Random;

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


    private WorkspaceCtrl workspaceCtrl;
    private Scene workspace;

    private EditCardCtrl editCardCtrl;
    private Scene editCard;


    private double windowHeight;
    private double windowWidth;


    public void initialize(Stage primaryStage,
            Pair<AddCardCtrl, Parent> addCard,
                           Pair<AddListCtrl, Parent> addList,
                           Pair<BoardOverviewCtrl, Parent> board,
                           Pair<WelcomePageCtrl, Parent> welcomePage,
                           Pair<EditListCtrl, Parent> editList,

                           Pair<WorkspaceCtrl, Parent> workspace,
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

        this.workspaceCtrl = workspace.getKey();
        this.workspace = new Scene(workspace.getValue());

        this.editListCtrl=editList.getKey();
        this.editList = new Scene(editList.getValue());

        this.editCardCtrl = editCard.getKey();
        this.editCard = new Scene(editCard.getValue());

        showWelcomePage();
        primaryStage.show();
    }


    /**
     * @return String: random String that will server as a Board password, it
     * can contain letters, numbers and special characters, and it is
     * of length 8-12 (randomly chosen)
     */
    private String generatePassword(){
        Random random = new Random();
        int length = random.nextInt(8,12);

        String characters = "abcdefghijklmnopqrstuvwxyz*!#@$ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder sb = new StringBuilder();

        for(int i =0;i<length;i++){
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
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

    }

    public void showNewBoard(){
        primaryStage.setTitle("Board");
        primaryStage.setScene(board);
        Board newBoard = new Board("Board",generatePassword());
        boardOverviewCtrl.setBoard(newBoard);
        boardOverviewCtrl.saveBoardInDatabase();
        boardOverviewCtrl.refresh();

    }

    public void showBoard() {
        //storeWindowSize(primaryStage.getScene());

        primaryStage.setTitle("Board Overview");
        primaryStage.setScene(board);
        boardOverviewCtrl.refresh();
        //setWindowSize();

    }

    public void showAddList(Board boardToAddTo) {
        primaryStage.setTitle("Adding List");
        primaryStage.setScene(addList);
        addListCtrl.setBoardToAddTo(boardToAddTo);
    }

    public void showEditList(BoardList boardListToEdit){
        primaryStage.setTitle("Editing List");
        primaryStage.setScene(editList);
        editListCtrl.setBoardListToEdit(boardListToEdit);
    }

    public void deleteList(){
        boardOverviewCtrl.refresh();
    }

    public void showWelcomePage() {
        primaryStage.setTitle("Welcome Page");
        primaryStage.setScene(welcomePage);
    }


    public void showWorkspace(Integer userID) {
        primaryStage.setTitle("Workspace");
        primaryStage.setScene(workspace);
        workspaceCtrl.setUser(userID);
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