package server.services;

import org.springframework.stereotype.Service;
import commons.Board;
import org.springframework.transaction.annotation.Transactional;
import server.database.BoardRepository;
import java.util.List;

@Service
public class BoardService {
    private final BoardRepository repo;

    /**
     * Constructor for BoardService which uses BoardRepository.
     * @param repo
     */
    public BoardService(BoardRepository repo) {
        this.repo = repo;
    }

    /**
     * Method which returns all boards.
     * @return
     */
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
    public Board add(Board board) throws Exception {
        if (board.title == null) {
            throw new Exception("Invalid title");
        }
        return this.repo.save(board);
    }

    /**
     * Method which deletes a board by id from repo.
     * @param id
     * @return the deleted board
     * @throws Exception if id is not in repo.
     */
    public Board deleteById(Integer id) throws Exception {
        if (id < 0 || !this.repo.existsById(id)) {
            throw new Exception("Invalid id");
        }
        Board deletedRecord = this.repo.findById(id).get();
        this.repo.deleteById(id);
        return deletedRecord;
    }

    @Transactional
    public Board updateTitleById(Integer id, String title) throws Exception{
        if (id < 0 || !repo.existsById(id)) {
            throw new Exception("Invalid id");
        }
        if (title == null || title.equals("") || title.contains("#")) {
            throw new Exception("Invalid title");
        }
        repo.updateBoardById(id, title);
        return repo.getById(id);
    }
    @Transactional
    public Board updatePasswordById(Integer id, String password) throws Exception{
        if (id < 0 || !repo.existsById(id)) {
            throw new Exception("Invalid id");
        }
        repo.updatePasswordById(id, password);
        return getById(id);
    }

    public Board updateColorBoardBackground(Integer id, String color) throws Exception {
        Board res = repo.findById(id).orElseThrow(
                ()->new Exception("Tag with id: " + id +" not found")
        );
        res.colorBoardBackground = color;
        return repo.save(res);
    }

    public Board updateColorBoardFont(Integer id, String color) throws Exception {
        Board res = repo.findById(id).orElseThrow(
                ()->new Exception("Tag with id: " + id +" not found")
        );
        res.colorBoardFont = color;
        return repo.save(res);
    }

    public Board updateColorListsBackground(Integer id, String color) throws Exception {
        Board res = repo.findById(id).orElseThrow(
                ()->new Exception("Tag with id: " + id +" not found")
        );
        res.colorListsBackground = color;
        return repo.save(res);
    }

    public Board updateColorListsFont(Integer id, String color) throws Exception {
        Board res = repo.findById(id).orElseThrow(
                ()->new Exception("Tag with id: " + id +" not found")
        );
        res.colorListsFont = color;
        return repo.save(res);
    }
}
