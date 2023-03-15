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

import commons.Board;
import commons.BoardList;
import commons.Card;
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
    //This does not work!!!

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

//    public List<BoardList> getBoardListById(){
//        return ClientBuilder.newClient(new ClientConfig())
//                .target(server).path("api/lists/")
//                .request(APPLICATION_JSON)
//                .accept(APPLICATION_JSON)
//                .get(new GenericType<List<BoardList>>() {});
//    }

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
        ///Need to edit to add id instead of list and also pass new title
    }


    // Dummy placeholder methods for getting and posting cards
    public List<Card> getCards(int listId) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/cards/list/"+listId) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Card>>() {});
    }

    public Card addCard(Card card) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/cards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(card, APPLICATION_JSON), Card.class);

    }

    //Changes the SERVER variable and updates it with the new server
    public void setServer(String chosenServer) {
        server = chosenServer;
    }
}