package server.services;

import commons.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.TestBoardRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardServiceTest {

    private TestBoardRepository repo;
    private BoardService sut;
    private Board b1, b2, b3;
    private List<Board> boards;

    @BeforeEach
    public void setUp() {
        repo = new TestBoardRepository();
        b1 = new Board("First", "123");
        b2 = new Board("Second", "456");
        b3 = new Board("Third", "789");
        this.boards = new ArrayList<>();
        boards.add(b1);
        boards.add(b2);
        boards.add(b3);
        repo.setBoards(boards);
        sut = new BoardService(repo);
    }

    @Test
    public void testConstructor() {
        assertNotNull(sut);
    }

    @Test
    public void testFindAll() {
        List<Board> result = sut.findAll();
        List<Board> expected = new ArrayList<>();
        expected.add(b1);
        expected.add(b2);
        expected.add(b3);
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestBoardRepository.FIND_ALL);
        assertEquals(expected, result);
        assertEquals(expectedCalls, repo.getCalls());
    }

    @Test
    public void testGetByIdSuccess() throws Exception {
        Board result = sut.getById(2);
        Board expected = b2;
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestBoardRepository.FIND_BY_ID);
        assertEquals(expected, result);
        assertEquals(expectedCalls, repo.getCalls());
    }

    @Test
    void add() {
    }

    @Test
    void deleteById() {
    }
}