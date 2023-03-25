package server.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.database.BoardListRepository;
import server.database.BoardRepository;

import commons.Board;
import commons.BoardList;

@Service
public class BoardListService {
    private final BoardListRepository boardListRepository;
    private final BoardRepository boardRepository;

    /**
     * Constructor for BoardListService which uses BoardListRepository and BoardRepository.
     * @param boardListRepository
     * @param boardRepository
     */
    public BoardListService(BoardListRepository boardListRepository,
                            BoardRepository boardRepository) {
        this.boardListRepository = boardListRepository;
        this.boardRepository = boardRepository;
    }

    /**
     * Method which returns all lists stored in repo.
     * @return
     */
    public List<BoardList> findAll() {
        return this.boardListRepository.findAll();
    }

    public BoardList findById(int id) {
        return this.boardListRepository.findById(id).orElse(null);
    }

    /**
     * Method which adds a new list to repo.
     * @param boardList
     * @return the saved list.
     * @throws Exception if title is not valid
     */
    public BoardList add(BoardList boardList) throws Exception {
        if (boardList.title == null) {
            throw new Exception("Invalid title");
        }
        Board board = this.boardRepository.findById(boardList.boardId).get();
        board.addBoardList(boardList);
        return this.boardListRepository.save(boardList);
    }

    /**
     * Method which deletes a list by id from repo.
     * @param id
     * @return the deleted list.
     * @throws Exception if list does not exist.
     */
    public BoardList deleteById(Integer id) throws Exception {
        if (id < 0 || !this.boardListRepository.existsById(id)) {
            throw new Exception("Invalid id");
        }
        BoardList deletedRecord = this.boardListRepository.findById(id).get();
        this.boardListRepository.deleteById(id);
        return deletedRecord;
    }

    /**
     * Method which updates title of existing list.
     * @param id
     * @param title
     * @return success message.
     * @throws Exception if list does not exist.
     * @throws Exception if title is not valid.
     */
    @Transactional
    public String updateTitleById(Integer id, String title) throws Exception{
        if (id < 0 || !this.boardListRepository.existsById(id)) {
            throw new Exception("Invalid id");
        }
        if (title == null) {
            throw new Exception("Invalid title");
        }
        this.boardListRepository.updateListById(id, title);
        return "List title has been updated successfully.";
    }
}
