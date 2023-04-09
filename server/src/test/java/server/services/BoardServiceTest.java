package server.services;

import commons.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.TestBoardRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardServiceTest {

    private TestBoardRepository repo;
    private BoardService sut;
    private Board b1, b2, b3;
    private List<Board> boards;

    @BeforeEach
    public void setUp() {
        repo = new TestBoardRepository();
        b1 = new Board(1, "First", "123", new ArrayList<>());
        b2 = new Board(2, "Second", "456", new ArrayList<>());
        b3 = new Board(3, "Third", "789", new ArrayList<>());
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
        expectedCalls.add(TestBoardRepository.EXISTS_BY_ID);
        expectedCalls.add(TestBoardRepository.FIND_BY_ID);
        assertEquals(expected, result);
        assertEquals(expectedCalls, repo.getCalls());
    }

    @Test
    public void testGetByIdInvalid() throws Exception {
        List<String> expectedCalls = new ArrayList<>();
        assertThrows(Exception.class, () -> sut.getById(-1));
        assertEquals(expectedCalls, repo.getCalls());
    }

    @Test
    public void testGetByIdNotExists() throws Exception {
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestBoardRepository.EXISTS_BY_ID);
        assertThrows(Exception.class, () -> sut.getById(100));
        assertEquals(expectedCalls, repo.getCalls());
    }

    @Test
    public void testAddSuccessful() throws Exception {
        Board b4 = new Board(4, "Fourth", "000", new ArrayList<>());
        Board expected = sut.add(b4);
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestBoardRepository.SAVE);
        assertEquals(expected, b4);
        assertEquals(expectedCalls, repo.getCalls());
    }

    @Test
    public void testAddInvalidTitle() throws Exception {
        Board b4 = new Board(4, null, "000", new ArrayList<>());
        List<String> expectedCalls = new ArrayList<>();
        assertThrows(Exception.class, () -> sut.add(b4));
        assertEquals(expectedCalls, repo.getCalls());
    }

    @Test
    public void testDeleteByIdSuccess() throws Exception {
        Board result = sut.deleteById(2);
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestBoardRepository.EXISTS_BY_ID);
        expectedCalls.add(TestBoardRepository.FIND_BY_ID);
        expectedCalls.add(TestBoardRepository.DELETE_BY_ID);
        assertEquals(b2, result);
        assertEquals(expectedCalls, repo.getCalls());
    }

    @Test
    public void testDeleteByIdInvalid() throws Exception {
        List<String> expectedCalls = new ArrayList<>();
        assertThrows(Exception.class, () -> sut.deleteById(-1));
        assertEquals(expectedCalls, repo.getCalls());
    }

    @Test
    public void testDeleteByIdNonExistent() throws Exception {
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestBoardRepository.EXISTS_BY_ID);
        assertThrows(Exception.class, () -> sut.deleteById(100));
        assertEquals(expectedCalls, repo.getCalls());
    }

    @Test
    public void testUpdatePasswordById() throws Exception {
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestBoardRepository.EXISTS_BY_ID);
        expectedCalls.add(TestBoardRepository.UPDATE_PASSWORD_BY_ID);
        sut.updatePasswordById(3, "DRUKAS");
        expectedCalls.add(TestBoardRepository.EXISTS_BY_ID);
        expectedCalls.add(TestBoardRepository.FIND_BY_ID);
        assertEquals(expectedCalls, repo.getCalls());
        assertEquals(sut.getById(3).password, "DRUKAS");
    }

    @Test
    public void testUpdateTitleById() throws Exception {
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestBoardRepository.EXISTS_BY_ID);
        expectedCalls.add(TestBoardRepository.UPDATE_BY_ID);
        expectedCalls.add(TestBoardRepository.GET_BY_ID);
        Board result = sut.updateTitleById(1, "New First Title");
        assertEquals(new Board(1, "New First Title", "123", new ArrayList<>()), result);
        assertEquals(expectedCalls, repo.getCalls());
    }

    @Test
    public void testUpdateTitleInvalidId() throws Exception {
        List<String> expectedCalls = new ArrayList<>();
        assertThrows(Exception.class, () -> sut.updateTitleById(-1, "New"));
        assertEquals(expectedCalls, repo.getCalls());
    }

    @Test
    public void testUpdateTitleNonExistent() throws Exception {
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestBoardRepository.EXISTS_BY_ID);
        assertThrows(Exception.class, () -> sut.updateTitleById(100, "New"));
        assertEquals(expectedCalls, repo.getCalls());
    }

    @Test
    public void testUpdateTitleInvalidText() throws Exception {
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestBoardRepository.EXISTS_BY_ID);
        assertThrows(Exception.class, () -> sut.updateTitleById(1, "#"));
        assertEquals(expectedCalls, repo.getCalls());

    }
}