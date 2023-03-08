package server.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import commons.Board;
import server.database.BoardRepository;

@Service
public class BoardService {

    /**
     * Method which returns a board by id from repo.
     * @param id
     * @param repo
     * @return board
     * @throws Exception if id is not in repo.
     */
    public ResponseEntity<Board> getById(Integer id, BoardRepository repo) throws Exception {
        if (id < 0 || !repo.existsById(id)) {
            throw new Exception("Invalid id");
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * Method which adds a new board to repo.
     * @param board
     * @param repo
     * @return the saved board
     * @throws Exception if title is null.
     */
    public ResponseEntity<Board> add(Board board, BoardRepository repo) throws Exception {
        if (board.title == null) {
            throw new Exception("Invalid title");
        }
        return ResponseEntity.ok(repo.save(board));
    }

    /**
     * Method which deletes a board by id from repo.
     * @param id
     * @param repo
     * @return the deleted board
     * @throws Exception if id is not in repo.
     */
    public ResponseEntity<Board> deleteById(Integer id, BoardRepository repo) throws Exception {
        if (id < 0 || !repo.existsById(id)) {
            throw new Exception("Invalid id");
        }
        ResponseEntity<Board> deletedRecord = ResponseEntity.ok(repo.findById(id).get());
        repo.deleteById(id);
        return deletedRecord;
    }
}
