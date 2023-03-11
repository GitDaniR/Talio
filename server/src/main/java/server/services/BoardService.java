package server.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import commons.Board;
import server.database.BoardRepository;

import java.util.List;

@Service
public class BoardService {
    private final BoardRepository repo;

    public BoardService(BoardRepository repo) {
        this.repo = repo;
    }

    public List<Board> findAll() {
        return this.repo.findAll();
    }

    /**
     * Method which returns a board by id from repo.
     * @param id
     * @return board
     * @throws Exception if id is not in repo.
     */
    public Board getById(Integer id) throws Exception {
        if (id < 0 || !this.repo.existsById(id)) {
            throw new Exception("Invalid id");
        }
        return this.repo.findById(id).get();
    }

    /**
     * Method which adds a new board to repo.
     * @param board
     * @return the saved board
     * @throws Exception if title is null.
     */
    public ResponseEntity<Board> add(Board board) throws Exception {
        if (board.title == null) {
            throw new Exception("Invalid title");
        }
        return ResponseEntity.ok(this.repo.save(board));
    }

    /**
     * Method which deletes a board by id from repo.
     * @param id
     * @return the deleted board
     * @throws Exception if id is not in repo.
     */
    public ResponseEntity<Board> deleteById(Integer id) throws Exception {
        if (id < 0 || !this.repo.existsById(id)) {
            throw new Exception("Invalid id");
        }
        ResponseEntity<Board> deletedRecord = ResponseEntity.ok(this.repo.findById(id).get());
        this.repo.deleteById(id);
        return deletedRecord;
    }
}
