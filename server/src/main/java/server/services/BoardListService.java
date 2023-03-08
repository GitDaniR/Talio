package server.services;

import commons.BoardList;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import server.database.BoardListRepository;

import java.util.List;

@Service
public class BoardListService {
    private final BoardListRepository repo;

    public BoardListService(BoardListRepository repo) {
        this.repo = repo;
    }

    public List<BoardList> findAll() {
        return this.repo.findAll();
    }

    public ResponseEntity<BoardList> add(BoardList boardList) throws Exception {
        if (boardList.title == null) {
            throw new Exception("Invalid title");
        }
        return ResponseEntity.ok(repo.save(boardList));
    }
}
