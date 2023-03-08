package server.services;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import server.database.BoardListRepository;
import server.database.BoardRepository;

import commons.Board;
import commons.BoardList;

@Service
public class BoardListService {
    private final BoardListRepository boardListRepository;
    private final BoardRepository boardRepository;

    public BoardListService(BoardListRepository boardListRepository, BoardRepository boardRepository) {
        this.boardListRepository = boardListRepository;
        this.boardRepository = boardRepository;
    }

    public List<BoardList> findAll() {
        return this.boardListRepository.findAll();
    }

    public ResponseEntity<BoardList> add(BoardList boardList) throws Exception {
        if (boardList.title == null) {
            throw new Exception("Invalid title");
        }
        Board board = this.boardRepository.findById(boardList.board.id).get();
        board.addBoardList(boardList);
        return ResponseEntity.ok(this.boardListRepository.save(boardList));
    }

    public ResponseEntity<BoardList> deleteById(Integer id) throws Exception {
        if (id < 0 || !this.boardListRepository.existsById(id)) {
            throw new Exception("Invalid id");
        }
        ResponseEntity<BoardList> deletedRecord = ResponseEntity.ok(this.boardListRepository.findById(id).get());
        this.boardListRepository.deleteById(id);
        return deletedRecord;
    }
}
