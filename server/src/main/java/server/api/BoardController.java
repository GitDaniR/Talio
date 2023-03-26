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
package server.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import commons.Board;
import server.services.BoardService;

@RestController
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boardService;
    private SimpMessagingTemplate msgs;

    /**
     * Constructor for BoardController which uses BoardService.
     * @param boardService
     */
    public BoardController(BoardService boardService,SimpMessagingTemplate msgs) {
        this.boardService = boardService;
        this.msgs = msgs;
    }

    /**
     * Method which returns all boards.
     * @return all boards
     */
    @GetMapping("/")
    public List<Board> getAll(){
        return this.boardService.findAll();
    }

    /**
     * Method which returns a board by an id.
     * @param id
     * @return a board
     */

    @GetMapping("/{id}")
    public ResponseEntity<Board> getById(@PathVariable("id") Integer id) {
        Board found;
        try {
            found = this.boardService.getById(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(found);
    }

    /**
     * Method which adds a new board.
     * Method which adds a new board to repo.
     * @param board
     * @return the saved board or BAD_REQUEST
     */
    @PostMapping("/")
    public ResponseEntity<Board> add(@RequestBody Board board) {
        Board saved;
        try {
            saved = this.boardService.add(board);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(saved);
    }

    /**
     * Method which deletes a board by id.
     * @param id
     * @return the deleted board or BAD_REQUEST
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Board> deleteById(@PathVariable("id") Integer id) {
        Board deletedRecord;
        try {
            deletedRecord = this.boardService.deleteById(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        if(msgs!=null)
            msgs.convertAndSend("/topic/boards/removed",id);

        return ResponseEntity.ok(deletedRecord);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Board> updateTitleById(@PathVariable("id") Integer id,
                                                  @RequestBody String title) {
        Board response;
        try {
            response = boardService.updateTitleById(id, title);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        if(msgs!=null)
            msgs.convertAndSend("/topic/boards/rename",response);

        return ResponseEntity.ok(response);
    }
}
