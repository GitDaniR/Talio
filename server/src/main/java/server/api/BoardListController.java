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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import server.database.BoardListRepository;

@RestController
@RequestMapping("/api/lists")
public class BoardListController {

    private final Random random;
    private final BoardListRepository repo;

    public BoardListController(Random random, BoardListRepository repo) {
        this.random = random;
        this.repo = repo;
    }

    @GetMapping(path = { "", "/" })
    public List<BoardList> getAll() {
        return repo.findAll();
    }

    @PostMapping(path = { "", "/" })
    public ResponseEntity<BoardList> add(@RequestBody BoardList list) {

        if (list.title == null) {
            return ResponseEntity.badRequest().build();
        }

        BoardList saved = repo.save(list);
        return ResponseEntity.ok(saved);
    }
}