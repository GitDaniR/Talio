package server.api;

import commons.Board;
import commons.BoardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.database.*;
import server.services.BoardListService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardListControllerTest {

    TestSimpMessagingTemplate simp;
    TestBoardRepository boardRepo;
    TestBoardListRepository listRepo;
    Board b1;
    BoardList l1, l2, l3;
    List<BoardList> lists;
    BoardListController sut;

    @BeforeEach
    void setUp() {
        simp = new TestSimpMessagingTemplate(new TestMessageChannel());
        boardRepo = new TestBoardRepository();
        listRepo = new TestBoardListRepository();
        b1 = new Board(0, "First Board", "123", new ArrayList<>());
        boardRepo.save(b1);
        l1 = new BoardList(0, "First List", b1, 0);
        l2 = new BoardList(1, "Second List", b1, 0);
        l3 = new BoardList(2, "Third List", b1, 0);
        lists = new ArrayList<>();
        lists.add(l1);
        lists.add(l2);
        lists.add(l3);
        listRepo.setBoardLists(lists);
        sut = new BoardListController(new BoardListService(listRepo, boardRepo), simp);
    }

    @Test
    void testGetAll() {
        List<BoardList> result = sut.getAll();
        List<BoardList> expected = new ArrayList<>();
        expected.add(new BoardList(0, "First List", b1, 0));
        expected.add(new BoardList(1, "Second List", b1, 0));
        expected.add(new BoardList(2, "Third List", b1, 0));
        assertEquals(expected, result);
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    void testAddSuccessful() {
        BoardList l4 = new BoardList(3, "Fourth List", b1, 0);
        ResponseEntity listResponse = sut.add(l4);
        assertEquals(HttpStatus.OK, listResponse.getStatusCode());
        assertEquals(new BoardList(3, "Fourth List", b1, 0), listResponse.getBody());
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add("/topic/lists");
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    void testAddNull() {
        ResponseEntity listResponse = sut.add(null);
        assertEquals(HttpStatus.BAD_REQUEST, listResponse.getStatusCode());
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    void testDeleteByIdSuccessful() {
        ResponseEntity listResponse = sut.deleteById(1);
        assertEquals(HttpStatus.OK, listResponse.getStatusCode());
        assertEquals(new BoardList(1, "Second List", b1, 0), listResponse.getBody());
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add("/topic/lists");
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    void testDeleteByIdNonExistend() {
        ResponseEntity listResponse = sut.deleteById(100);
        assertEquals(HttpStatus.BAD_REQUEST, listResponse.getStatusCode());
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    void testUpdateTitleByIdSuccessful() {
        ResponseEntity listResponse = sut.updateTitleById(0, "New First List");
        assertEquals(HttpStatus.OK, listResponse.getStatusCode());
        assertEquals("List title has been updated successfully.", listResponse.getBody());
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add("/topic/lists/rename");
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    void testUpdateTitleByIdInvalidTitle() {
        ResponseEntity listResponse = sut.updateTitleById(0, null);
        assertEquals(HttpStatus.BAD_REQUEST, listResponse.getStatusCode());
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, simp.getDestinations());
    }
}