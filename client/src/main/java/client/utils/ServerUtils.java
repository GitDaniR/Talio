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
package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.MediaType.TEXT_PLAIN;

import java.util.List;

import commons.*;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;

public class ServerUtils {

    private static String server = "http://localhost:8080/";

    public List<Board> getBoards(){
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/boards/")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Board>>() {});
    }

    public Board addBoard(Board board){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/boards/") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(board, APPLICATION_JSON), Board.class);
    }
    public Board getBoardByID(int id){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/boards/"+id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<Board>() {});
    }

    public List<BoardList> getBoardLists() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/lists") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<BoardList>>() {});
    }

    public BoardList getBoardListById(int id){
        List<BoardList> lists = getBoardLists();
        for(BoardList l: lists){
            if(l.id == id) return l;
        }
        //just throwing an unchecked exception for when the board is not found
        throw new NullPointerException();
//        return ClientBuilder.newClient(new ClientConfig())
//                .target(server).path("api/lists/" + id)
//                .request(APPLICATION_JSON)
//                .accept(APPLICATION_JSON)
//                .get(new GenericType<BoardList>() {});
    }

    public BoardList addBoardList(BoardList list) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/lists") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(list, APPLICATION_JSON), BoardList.class);
    }

    public void deleteBoardList(Integer id){
        ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/lists/"+id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete();
    }

    public void deleteCard(Integer id){
        ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/cards/"+id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete();

    }

    public void updateBoardListTitle(Integer id, String title){
        ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/lists/"+id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(title, TEXT_PLAIN), String.class);

    }

    public void updateBoardTitle(Integer id, String title){
        ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/boards/"+id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(title, TEXT_PLAIN), String.class);

    }

    public List<Card> getCards(int listId) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/cards/list/"+listId) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Card>>() {});
    }

    public Card getCardById(int id) {
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("api/cards/"+id) //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .get(new GenericType<Card>() {});
    }

    public Card addCard(Card card) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/cards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    public void editCard(Integer id, Card card) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/cards/"+id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    //Changes the SERVER variable and updates it with the new server
    public void setServer(String chosenServer) {
        server = chosenServer;
    }

    public User addUser(User user){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/users") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(user, APPLICATION_JSON), User.class);

    }

    /**
     * Server method to return the user based on userID
     * @param userID - userID for the User table in databse
     * @return - User object correcsponding to user
     * havin id: userID
     */
    public User getUserById(int userID){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/users/"+userID) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<User>() {});

    }

    /**
     * Method that gets the user by its username
     * @param username
     * @return
     */
    public User getUserByUsername(String username){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/users/username/"+username) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<User>() {});
    }

    /**
     * Method that assigns board with the BoardID to the list of
     * joined boards by the user with UserID
     * @param userID - id of the user in the database
     * @param boardID - id of the board in the database
     * @return
     */
    public User assignBoardToUser(Integer userID, Integer boardID){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/users/"+userID+"/boards/"+boardID) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(userID, APPLICATION_JSON), User.class);

    }

    /**
     * Method which deletes board from list of joined boards by the user
     * @param userId -  id of the user
     * @param boardId - id of the board
     */
    public void removeBoardFromJoined(Integer userId, Integer boardId){
        ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/users/"+userId+"/boards/"+boardId) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete();

    }

    public void deleteBoard(Integer boardId){
        ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/boards/"+boardId) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete();
    }

    /**
     * Method that sends request to the server to add Subtask
     * @param subtask - Subtask entity to be added to the database
     * @return
     */
    public Subtask addSubtask(Subtask subtask){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/subtasks") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(subtask, APPLICATION_JSON), Subtask.class);
    }

    /**
     * Method that sends request to the server to
     * delete the subtask with id -> subtaskId
     * @param subtaskId - id of the subtask entity
     *                  to be deleted
     */
    public void removeSubtask(Integer subtaskId){
        ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/subtasks/"+subtaskId) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete();
    }

    /**
     * Method that sends request to the server to
     * update the status of the subtask to status done
     * @param subtaskId - id of the subtask entity
     *                  in the database
     * @param done - status of the subtask to be changed to
     */
    public void updateSubtaskStatus(Integer subtaskId, Boolean done){
        ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/subtasks/"+subtaskId) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(String.valueOf(done),APPLICATION_JSON), String.class);
    }

}