package server.api;

import commons.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.database.TestBoardRepository;
import server.database.TestMessageChannel;
import server.database.TestSimpMessagingTemplate;
import server.services.BoardService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardControllerTest {

    private TestSimpMessagingTemplate simp;
    private TestBoardRepository boardRepo;
    private BoardController sut;
    private Board b1, b2, b3;
    private List<Board> boards;

    @BeforeEach
    void setUp() {
        simp = new TestSimpMessagingTemplate(new TestMessageChannel());
        boardRepo = new TestBoardRepository();
        b1 = new Board(0, "First Board", "123", new ArrayList<>());
        b2 = new Board(1, "Second Board", "456", new ArrayList<>());
        b3 = new Board(2, "Third Board", "789", new ArrayList<>());
        boards = new ArrayList<>();
        boards.add(b1);
        boards.add(b2);
        boards.add(b3);
        boardRepo.setBoards(boards);
        sut = new BoardController(new BoardService(boardRepo), simp);
    }

    @Test
    void testGetAll() {
        List<Board> res = sut.getAll();
        List<Board> expected = new ArrayList<>();
        expected.add(new Board(0, "First Board", "123", new ArrayList<>()));
        expected.add(new Board(1, "Second Board", "456", new ArrayList<>()));
        expected.add(new Board(2, "Third Board", "789", new ArrayList<>()));
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expected, res);
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    void testGetByIdSuccessful() {
        ResponseEntity<Board> boardResponse = sut.getById(0);
        assertEquals(HttpStatus.OK, boardResponse.getStatusCode());
        assertEquals(new Board(0, "First Board", "123", new ArrayList<>()), boardResponse.getBody());
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    void testGetByIdNotFound() {
        ResponseEntity<Board> boardResponse = sut.getById(100);
        assertEquals(HttpStatus.BAD_REQUEST, boardResponse.getStatusCode());
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    void testAddSuccessful() {
        Board b4 = new Board(3, "New Board", "000", new ArrayList<>());
        ResponseEntity<Board> boardResponse = sut.add(b4);
        assertEquals(HttpStatus.OK, boardResponse.getStatusCode());
        assertEquals(new Board(3, "New Board", "000", new ArrayList<>()), boardResponse.getBody());
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    void testAddNull() {
        ResponseEntity<Board> boardResponse = sut.add(null);
        assertEquals(HttpStatus.BAD_REQUEST, boardResponse.getStatusCode());
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    void testDeleteById() {
        ResponseEntity<Board> boardResponse = sut.deleteById(1);
        assertEquals(HttpStatus.OK, boardResponse.getStatusCode());
        assertEquals(new Board(1, "Second Board", "456", new ArrayList<>()), boardResponse.getBody());
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add("/topic/boards/removed");
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    void testDeleteByIdNotFound() {
        ResponseEntity<Board> boardResponse = sut.deleteById(100);
        assertEquals(HttpStatus.BAD_REQUEST, boardResponse.getStatusCode());
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    void testUpdateTitleById() {
        ResponseEntity<Board> boardResponse = sut.updateTitleById(0, "New First Title");
        assertEquals(HttpStatus.OK, boardResponse.getStatusCode());
        assertEquals(new Board(0, "New First Title", "123", new ArrayList<>()), boardResponse.getBody());
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add("/topic/boards/rename");
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    void testUpdateTitleByIdInvalidTitle() {
        ResponseEntity<Board> boardResponse = sut.updateTitleById(0, null);
        assertEquals(HttpStatus.BAD_REQUEST, boardResponse.getStatusCode());
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, simp.getDestinations());
    }
}