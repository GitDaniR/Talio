package server.api;

import commons.Board;
import commons.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.database.TestBoardRepository;
import server.database.TestMessageChannel;
import server.database.TestSimpMessagingTemplate;
import server.database.TestUserRepository;
import server.services.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserControllerTest {

    private TestSimpMessagingTemplate simp;
    private TestUserRepository userRepo;
    private TestBoardRepository boardRepo;
    private UserController sut;
    private Board b1;
    private User u1, u2, u3;
    private List<User> users;

    @BeforeEach
    void setUp() {
        simp = new TestSimpMessagingTemplate(new TestMessageChannel());
        userRepo = new TestUserRepository();
        boardRepo = new TestBoardRepository();
        b1 = new Board(0, "First Board", "123", new ArrayList<>());
        boardRepo.save(b1);
        u1 = new User(0, "First User");
        u2 = new User(1, "Second User");
        u3 = new User(2, "Third User");
        users = new ArrayList<>();
        users.add(u1);
        users.add(u2);
        users.add(u3);
        userRepo.setUsers(users);
        sut = new UserController(new UserService(userRepo, boardRepo), simp);
    }

    @Test
    void testGetAll() {
        List<User> res = sut.getAll();
        List<User> expected = new ArrayList<>();
        expected.add(new User(0, "First User"));
        expected.add(new User(1, "Second User"));
        expected.add(new User(2, "Third User"));
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expected, res);
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    void testGetByIdSuccessful() {
        ResponseEntity userResponse = sut.getById(0);
        assertEquals(HttpStatus.OK, userResponse.getStatusCode());
        assertEquals(new User(0, "First User"), userResponse.getBody());
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    void testGetByIdNonExistent() {
        ResponseEntity userResponse = sut.getById(100);
        assertEquals(HttpStatus.BAD_REQUEST, userResponse.getStatusCode());
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    void testGetByUsernameSuccessful() {
        ResponseEntity userResponse = sut.getByUsername("First User");
        assertEquals(HttpStatus.OK, userResponse.getStatusCode());
        assertEquals(new User(0, "First User"), userResponse.getBody());
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    void testGetByUsernameNonExistent() {
        ResponseEntity userResponse = sut.getByUsername("");
        assertEquals(HttpStatus.BAD_REQUEST, userResponse.getStatusCode());
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    void testAddSuccessful() {
        User u4 = new User(3, "Fourth User");
        ResponseEntity userResponse = sut.add(u4);
        assertEquals(HttpStatus.OK, userResponse.getStatusCode());
        assertEquals(new User(3, "Fourth User"), userResponse.getBody());
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    void testAddNull() {
        ResponseEntity userResponse = sut.add(null);
        assertEquals(HttpStatus.BAD_REQUEST, userResponse.getStatusCode());
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    void testDeleteByIdSuccessful() {
        ResponseEntity userResponse = sut.deleteById(1);
        assertEquals(HttpStatus.OK, userResponse.getStatusCode());
        assertEquals(new User(1, "Second User"), userResponse.getBody());
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    void testDeleteByIdNonExistent() {
        ResponseEntity userResponse = sut.deleteById(100);
        assertEquals(HttpStatus.BAD_REQUEST, userResponse.getStatusCode());
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    void testJoinBoardSuccessful() {
        ResponseEntity userResponse = sut.joinBoard(0, 0);
        assertEquals(HttpStatus.OK, userResponse.getStatusCode());
        assertEquals(new User(0, "First User"), userResponse.getBody());
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    void testJoinBoardInvalidId() {
        ResponseEntity userResponse = sut.joinBoard(0, -1);
        assertEquals(HttpStatus.BAD_REQUEST, userResponse.getStatusCode());
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    void testRemoveBoardSuccessful() {
        b1.users.add(u1);
        ResponseEntity userResponse = sut.removeBoard(0, 0);
        assertEquals(HttpStatus.OK, userResponse.getStatusCode());
        assertEquals(new User(0, "First User"), userResponse.getBody());
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    void testRemoveBoardInvalidId() {
        b1.users.add(u1);
        ResponseEntity userResponse = sut.removeBoard(0, 100);
        assertEquals(HttpStatus.BAD_REQUEST, userResponse.getStatusCode());
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, simp.getDestinations());
    }
}