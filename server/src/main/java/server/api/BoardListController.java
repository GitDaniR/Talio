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
import java.util.Random;
import commons.BoardList;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import server.database.BoardListRepository;
import server.services.BoardListService;

@RestController
@RequestMapping("/api/lists")
public class BoardListController {
    private final BoardListService boardListService;

    public BoardListController(BoardListService boardListService) {
        this.boardListService = boardListService;
    }
    @GetMapping("/")
    public List<BoardList> getAll() {
        return boardListService.findAll();
    }

    @PostMapping("/")
    public ResponseEntity<BoardList> add(@RequestBody BoardList boardList) {
        ResponseEntity<BoardList> saved;
        try {
            saved = this.boardListService.add(boardList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return saved;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BoardList> deleteById(@PathVariable("id") Integer id) {
        ResponseEntity<BoardList> deletedRecord;
        try {
            deletedRecord = this.boardListService.deleteById(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return deletedRecord;
    }

}