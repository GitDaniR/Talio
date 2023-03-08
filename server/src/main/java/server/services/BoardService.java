package server.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import commons.Board;
import server.database.BoardRepository;

@Service
public class BoardService {

    public ResponseEntity<Board> getById(Integer id, BoardRepository repo) throws Exception {
        if (id < 0 || !repo.existsById(id)) {
            throw new Exception("Invalid id");
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    public ResponseEntity<Board> add(Board board, BoardRepository repo) throws Exception {
        if (board.title == null) {
            throw new Exception("Invalid title");
        }
        return ResponseEntity.ok(repo.save(board));
    }

    public ResponseEntity<Board> deleteById(Integer id, BoardRepository repo) throws Exception {
        if (id < 0 || !repo.existsById(id)) {
            throw new Exception("Invalid id");
        }
        ResponseEntity<Board> deletedRecord = ResponseEntity.ok(repo.findById(id).get());
        repo.deleteById(id);
        return deletedRecord;
    }
}
