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

import commons.*;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.MediaType.TEXT_PLAIN;

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
                .target(server).path("api/cards/") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    public Card updateCardList(Card card, Integer listID, Integer index){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/cards/"+card.id+"/list/"+listID+"/"+index) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(listID, APPLICATION_JSON), Card.class);

    }

    public Card editCard(Integer id, Card card) {
        return ClientBuilder.newClient(new ClientConfig()) //
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
     * @param userID - userID for the User table in database
     * @return - User object corresponding to user
     * having id: userID
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

    public void deleteTag(Integer tagId) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/tags/" + tagId) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete();

    }

    public void editTagTitle(Integer tagId, String newTitle) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/tags/title/"+tagId) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(newTitle, APPLICATION_JSON), Tag.class);
    }

    public void editTagColor(Integer tagId, String newColor) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/tags/color/"+tagId) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(newColor, APPLICATION_JSON), Tag.class);
    }

    public void editTagFont(Integer tagId, String newColor) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/tags/font/"+tagId) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(newColor, APPLICATION_JSON), Tag.class);
    }

    public Tag getTagById(Integer tagId) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/tags/"+tagId) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<Tag>() {});
    }

    public Tag addTag(Tag tag) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/tags") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(tag, APPLICATION_JSON), Tag.class);
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
                .target(server).path("api/subtasks/status/"+subtaskId) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(String.valueOf(done),APPLICATION_JSON), String.class);
    }

    /**
     * Method that sends PUT request to the server to
     * update the title of the subtask.
     * @param id - id of the subtask
     * @param title - new title of the subtask
     */
    public void updateSubtaskTitle(Integer id, String title){
        ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/subtasks/title/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(title, APPLICATION_JSON), String.class);
    }

    /**
     * Method that sends PUT request to the server to
     * update the index of the subtask.
     * @param id - id of the subtask
     * @param index - new index of the subtask
     */
    public void updateSubtaskIndex(Integer id, Integer index){
        ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/subtasks/index/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(String.valueOf(index), APPLICATION_JSON), String.class);
    }

    private StompSession session;

    public void initializeStompSession(String url){
        session = connect("ws://"+url+"/websocket");
    }

    private StompSession connect(String url){
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try{
            return stomp.connect(url, new StompSessionHandlerAdapter() {}).get();
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        } catch (ExecutionException e){
            throw new RuntimeException();
        }
        throw new IllegalStateException();
    }

    public <T> void registerForMessages(String dest, Class<T> type, Consumer<T> consumer){
        session.subscribe(dest, new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return type;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                consumer.accept((T) payload);
            }
        });
    }

    public List<Tag> getTags(int boardId) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/tags/board/"+boardId)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Tag>>() {});
    }

    public void detachTag(Integer cardId, Tag tag) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/cards/tags/remove/"+cardId) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(tag, APPLICATION_JSON), Tag.class);
    }

    public List<Card> getAllCards() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/cards/")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Card>>() {});
    }

    public void editColorBoardBackground(Integer boardId, String newColor) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/boards/"+boardId+"/colors/boardBackground") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(newColor, APPLICATION_JSON), Board.class);
    }

    public void editColorBoardFont(Integer boardId, String newColor) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/boards/"+boardId+"/colors/boardFont") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(newColor, APPLICATION_JSON), Board.class);
    }

    public void editColorListsBackground(Integer boardId, String newColor) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/boards/"+boardId+"/colors/listsBackground") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(newColor, APPLICATION_JSON), Board.class);
    }

    public void editColorListsFont(Integer boardId, String newColor) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/boards/"+boardId+"/colors/listsFont") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(newColor, APPLICATION_JSON), Board.class);
    }
    public List<Preset> getAllBoardPresets(int boardId) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(server).path("api/presets/")
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(new GenericType<List<Preset>>() {}).stream().
            filter(preset->preset.boardId == boardId).
            collect(Collectors.toList());
    }
    public void editPresetBackground(Integer presetId, String newColor) {
        ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("api/presets/"+presetId+"/background/" + newColor) //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .put(Entity.entity(newColor, APPLICATION_JSON), String.class);
    }

    public void editPresetFont(Integer presetId, String newColor) {
        ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("api/presets/"+presetId+"/font/" + newColor) //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .put(Entity.entity(newColor, APPLICATION_JSON), String.class);
    }

    public void deletePreset(Integer presetId) {
        ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("api/presets/" + presetId) //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .delete();
    }

    public void setDefaultPreset(Integer presetId) {
        ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("api/presets/"+presetId+"/default/") //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .put(Entity.entity("", APPLICATION_JSON), String.class);
    }

    public Preset getPresetById(Integer presetId) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/presets/"+presetId) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<Preset>() {});
    }

    public Preset addPreset(Preset preset) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/presets/") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(preset, APPLICATION_JSON), Preset.class);
    }

//    public void send(String dest, Object o){
//        session.send(dest,o);
//    }
}