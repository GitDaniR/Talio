package server.services;

import commons.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.TestBoardRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardServiceTest {

    private TestBoardRepository repo;
    private BoardService sut;
    private Board b1, b2, b3;
    private List<Board> boards;

    @BeforeEach
    void setUp() {
        repo = new TestBoardRepository();
        b1 = new Board("First", "123");
        b2 = new Board("Second", "456");
        b3 = new Board("Third", "789");
        boards.add(b1);
        boards.add(b2);
        boards.add(b3);
        repo.setBoards(boards);
        sut = new BoardService(repo);
    }

    @Test
    void findAll() {
    }

    @Test
    void getById() {
    }

    @Test
    void add() {
    }

    @Test
    void deleteById() {
    }
}