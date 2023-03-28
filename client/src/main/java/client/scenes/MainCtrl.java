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

import commons.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.util.Random;
import java.util.Timer;

public class MainCtrl {

    private Stage primaryStage;

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

    private WorkspaceAdminCtrl workspaceAdminCtrl;
    private Scene workspaceAdmin;

    private ChangeBoardTitleCtrl changeBoardTitleCtrl;
    private Scene changeBoardTitle;

    private EditCardCtrl editCardCtrl;
    private Scene editCard;

    private EditTagCtrl editTagCtrl;
    private Scene editTag;
    private AddTagCtrl addTagCtrl;
    private Scene addTag;
    private TagOverviewCtrl tagOverviewCtrl;
    private Scene tagOverview;

    private AddRemoveTagsCtrl addRemoveTagsCtrl;
    private Scene addRemoveTags;

    private double windowHeight;
    private double windowWidth;

    //Maintain the current running timer so se can stop it when changing views/exiting the app
    private Timer currentTimer;

    private String username;

    private boolean isAdmin = false;

    //a const to easily manage the refresh rate of auto-sync
    public static final int REFRESH_RATE = 1000;

    public void initialize(Stage primaryStage,
                           Pair<AddListCtrl, Parent> addList,
                           Pair<BoardOverviewCtrl, Parent> board,
                           Pair<WelcomePageCtrl, Parent> welcomePage,
                           Pair<EditListCtrl, Parent> editList,
                           Pair<WorkspaceCtrl, Parent> workspace,
                           Pair<WorkspaceAdminCtrl, Parent> workspaceAdmin,
                           Pair<EditCardCtrl, Parent> editCard,
                           Pair<ChangeBoardTitleCtrl, Parent> changeBoardTitle,
                           Pair<EditTagCtrl, Parent> editTag,
                           Pair<AddTagCtrl, Parent> addTag,
                           Pair<TagOverviewCtrl, Parent> tagOverview,
                           Pair<AddRemoveTagsCtrl, Parent> addRemoveTags) {

        this.primaryStage = primaryStage;

        this.boardOverviewCtrl = board.getKey();
        this.board = new Scene(board.getValue());

        this.addListCtrl = addList.getKey();
        this.addList = new Scene(addList.getValue());

        this.welcomePageCtrl = welcomePage.getKey();
        this.welcomePage = new Scene(welcomePage.getValue());

        this.workspaceCtrl = workspace.getKey();
        this.workspace = new Scene(workspace.getValue());

        this.workspaceAdminCtrl = workspaceAdmin.getKey();
        this.workspaceAdmin = new Scene(workspaceAdmin.getValue());

        this.editListCtrl=editList.getKey();
        this.editList = new Scene(editList.getValue());

        this.changeBoardTitleCtrl = changeBoardTitle.getKey();
        this.changeBoardTitle = new Scene(changeBoardTitle.getValue());

        this.editCardCtrl = editCard.getKey();
        this.editCard = new Scene(editCard.getValue());

        this.editTagCtrl = editTag.getKey();
        this.editTag = new Scene(editTag.getValue());

        this.addTagCtrl = addTag.getKey();
        this.addTag = new Scene(addTag.getValue());

        this.tagOverviewCtrl = tagOverview.getKey();
        this.tagOverview = new Scene(tagOverview.getValue());

        this.addRemoveTagsCtrl = addRemoveTags.getKey();
        this.addRemoveTags = new Scene(addRemoveTags.getValue());

        showWelcomePage();
        primaryStage.show();
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
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

    /**
     * Method that opens new(empty) board for the user
     * @param user - user creating the board
     */
    public void showNewBoard(User user){
        primaryStage.setTitle("Board Overview");
        primaryStage.setScene(board);
        Board newBoard = new Board("Board",generatePassword());
        boardOverviewCtrl.setBoard(newBoard);
        boardOverviewCtrl.saveBoardInDatabase();
        boardOverviewCtrl.assignToUser(user);
        boardOverviewCtrl.refresh();
    }

    /**
     * Method that sets the current board as the
     * newly joined board and assigns it to the user
     * @param user - user joining the board
     * @param chosenBoard - board that the user is joining
     */
    public void joinBoard(User user, Board chosenBoard) {
        primaryStage.setTitle("Board Overview");
        primaryStage.setScene(board);
        boardOverviewCtrl.setBoard(chosenBoard);
        boardOverviewCtrl.assignToUser(user);
        boardOverviewCtrl.refresh();
    }

    /**
     * Method that returns to workspace and deletes the board
     * it was on previously
     */
    public void deleteBoard(){
        workspaceCtrl.clearJoinedBoards();

        if(!isAdmin){
            primaryStage.setTitle("Workspace");
            primaryStage.setScene(workspace);
            workspaceCtrl.refresh();
        } else {
            primaryStage.setTitle("Admin Workspace");
            primaryStage.setScene(workspaceAdmin);
            workspaceAdminCtrl.refresh();
        }
    }

    /**
     * Method that starts the scene showing the board
     */
    public void showBoard(Board showBoard) {
        primaryStage.setTitle("Board Overview");
        primaryStage.setScene(board);
        boardOverviewCtrl.setBoard(showBoard);
        boardOverviewCtrl.refresh();

    }

    public void showBoard(){
        primaryStage.setTitle("Board overview");
        primaryStage.setScene(board);
    }

    public void showChangeTitle(Board board){
        primaryStage.setTitle("Changing Board Title");
        primaryStage.setScene(changeBoardTitle);
        changeBoardTitleCtrl.setBoard(board);
    }

    /**
     * Method that starts adding list scene
     * @param boardToAddTo - board that you add lists to
     */
    public void showAddList(Board boardToAddTo) {
        primaryStage.setTitle("Adding List");
        primaryStage.setScene(addList);
        addListCtrl.setBoardToAddTo(boardToAddTo);
    }

    /**
     * Method that starts editing list scene
     * @param boardListToEdit - BoardList to be edited
     */
    public void showEditList(BoardList boardListToEdit){
        primaryStage.setTitle("Editing List");
        primaryStage.setScene(editList);
        editListCtrl.setBoardListToEdit(boardListToEdit);
    }

    /**
     * Method that starts welcome page
     */
    public void showWelcomePage() {
        primaryStage.setTitle("Welcome Page");
        primaryStage.setScene(welcomePage);
        welcomePageCtrl.clearPassword();
    }

    /**
     * Method that starts workspace scene for the user
     * @param username - username of the user
     */
    public void showWorkspace(String username) {
        if(isAdmin)
            showAdminWorkspace(username);
        else {
            primaryStage.setTitle("Workspace");
            primaryStage.setScene(workspace);
            workspaceCtrl.setUser(username);

            this.username = username;
            workspaceCtrl.refresh();
        }
    }

    public String getUsername() {
        return username;
    }

    /**
     * Method that starts the editing scene for the card
     * @param cardToEdit - card to be edited
     */
    public void showEditCard(Card cardToEdit) {
        primaryStage.setTitle("Editing Card");
        primaryStage.setScene(editCard);
        editCardCtrl.setCardToEdit(cardToEdit);
    }

    public void showAdminWorkspace(String username) {
        primaryStage.setTitle("Admin Workspace");
        primaryStage.setScene(workspaceAdmin);
        workspaceAdminCtrl.setUser(username);
        this.username = username;

        workspaceAdminCtrl.refresh();
    }

    public void showTagOverview(Board board) {
        primaryStage.setTitle("Tag Overview");
        primaryStage.setScene(tagOverview);
        tagOverviewCtrl.setBoard(board);

        tagOverviewCtrl.refresh();
    }

    public void showAddTag(Board board) {
        primaryStage.setTitle("Adding Tag");
        primaryStage.setScene(addTag);
        addTagCtrl.setBoard(board);
    }

    public void showEditTag(Tag tagToEdit) {
        primaryStage.setTitle("Editing Tag");
        primaryStage.setScene(editTag);
        editTagCtrl.setTagToEdit(tagToEdit);
    }

    public void showAddRemoveTags(Card card) {
        primaryStage.setTitle("Choosing Tags");
        primaryStage.setScene(addRemoveTags);
        addRemoveTagsCtrl.setCardToEdit(card);
        addRemoveTagsCtrl.setTags();
    }
}

