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
import org.springframework.web.bind.annotation.*;

import commons.Board;
import server.database.BoardRepository;
import server.services.BoardService;

@RestController
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boardService;
    private final BoardRepository repo;

    /**
     * Constructor for BoardController which uses BoardService and BoardRepository.
     * @param boardService
     * @param repo
     */
    public BoardController(BoardService boardService, BoardRepository repo) {
        this.boardService = boardService;
        this.repo = repo;
    }

    /**
     * Method which returns all boards in repo.
     * @return all boards
     */
    @GetMapping("/")
    public List<Board> getAll(){
        return repo.findAll();
    }

    /**
     * Method which returns a board by an id from repo.
     * @param id
     * @return a board
     */
    @GetMapping("/{id}")
    public ResponseEntity<Board> getById(@PathVariable("id") Integer id) {
        ResponseEntity<Board> found;
        try {
            found = this.boardService.getById(id, repo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return found;
    }

    /**
     * Method which adds a new board to repo.
     * @param board
     * @return the saved board or BAD_REQUEST
     */
    @PostMapping("/")
    public ResponseEntity<Board> add(@RequestBody Board board) {
        ResponseEntity<Board> saved;
        try {
            saved = this.boardService.add(board, repo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return saved;
    }

    /**
     * Method which deletes a board by id from repo.
     * @param id
     * @return the deleted board or BAD_REQUEST
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Board> deleteById(@PathVariable("id") Integer id) {
        ResponseEntity<Board> deletedRecord;
        try {
            deletedRecord = this.boardService.deleteById(id, repo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return deletedRecord;
    }
}
