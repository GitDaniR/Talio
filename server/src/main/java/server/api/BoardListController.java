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
import commons.BoardList;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import server.services.BoardListService;

@RestController
@RequestMapping("/api/lists")
public class BoardListController {
    private final BoardListService boardListService;

    /**
     * Constructor for BoardListController which uses boardListService.
     * @param boardListService
     */
    public BoardListController(BoardListService boardListService) {
        this.boardListService = boardListService;
    }

    /**
     * Method which returns all lists.
     * @return
     */
    @GetMapping(path = { "", "/" })
    public List<BoardList> getAll() {
        return boardListService.findAll();
    }

    /**
     * Method which adds a new list to repo.
     * @param boardList
     * @return
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<BoardList> add(@RequestBody BoardList boardList) {
        BoardList saved;
        try {
            saved = this.boardListService.add(boardList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(saved);
    }

    /**
     * Method which deletes a board by id from repo.
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<BoardList> deleteById(@PathVariable("id") Integer id) {
        BoardList deletedRecord;
        try {
            deletedRecord = this.boardListService.deleteById(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(deletedRecord);
    }

    /**
     * Method which updates a list title by id.
     * @param id
     * @param title
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateTitleById(@PathVariable("id") Integer id,
                                                  @RequestBody String title) {
        String response;
        try {
            response = this.boardListService.updateTitleById(id, title);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(response);
    }
}