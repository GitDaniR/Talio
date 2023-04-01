package server.api;

import commons.Card;
import commons.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.database.TestMessageChannel;
import server.database.TestSimpMessagingTemplate;
import server.database.TestSubtaskRepository;
import server.services.SubtaskService;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SubtaskControllerTest {

    private TestSimpMessagingTemplate simp;
    private TestSubtaskRepository repo;
    private SubtaskController sut;

    private Subtask s1, s2, s3;
    List<Subtask> subtasks;

    @BeforeEach
    void setup(){
        simp = new TestSimpMessagingTemplate(new TestMessageChannel());
        repo = new TestSubtaskRepository();
        s1 = new Subtask("Subtask 1", false, 0, null, 0);
        s1.id = 1;
        s2 = new Subtask("Subtask 2", false, 1, null, 0);
        s2.id = 2;
        s3 = new Subtask("Subtask 3", false, 2, null, 0);
        s3.id = 3;
        subtasks = new ArrayList<>();
        subtasks.add(s1);
        subtasks.add(s2);
        subtasks.add(s3);
        repo.setSubtasks(subtasks);
        sut = new SubtaskController(new SubtaskService(repo), simp);
    }

    @Test
    public void constructorTest(){
        assertNotNull(sut);
    }

    @Test
    public void getAllTest(){
        List<Subtask> expected = new ArrayList<>();
        Subtask test1 = new Subtask("Subtask 1", false, 0, null, 0);
        test1.id = 1;
        Subtask test2 = new Subtask("Subtask 2", false, 1, null, 0);
        test2.id = 2;
        Subtask test3 = new Subtask("Subtask 3", false, 2, null, 0);
        test3.id = 3;
        expected.add(test1);
        expected.add(test2);
        expected.add(test3);
        List<Subtask> res = sut.getAll();
        assertEquals(expected, res);
    }

    @Test
    void getByIdTest(){
        ResponseEntity<Subtask> result = sut.getById(2);
        Subtask expected = new Subtask("Subtask 2", false, 1, null, 0);
        expected.id = 2;
        assertEquals(ResponseEntity.ok(expected), result);
    }

    @Test
    void getByIdTest2(){
        ResponseEntity<Subtask> result = sut.getById(12);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void addTest(){
        Subtask s4 = new Subtask("Subtask 4", false, 3, null, 0);
        s4.id = 4;
        ResponseEntity<Subtask> result = sut.add(s4);

        List<Subtask> expected = new ArrayList<>();
        Subtask test1 = new Subtask("Subtask 1", false, 0, null, 0);
        test1.id = 1;
        Subtask test2 = new Subtask("Subtask 2", false, 1, null, 0);
        test2.id = 2;
        Subtask test3 = new Subtask("Subtask 3", false, 2, null, 0);
        test3.id = 3;
        Subtask test4 = new Subtask("Subtask 4", false, 3, null, 0);
        test4.id = 4;
        expected.add(test1);
        expected.add(test2);
        expected.add(test3);
        expected.add(test4);
        List<Subtask> res = sut.getAll();
        assertEquals(ResponseEntity.ok(s4), result);
        assertEquals(expected, res);

        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add("/topic/subtasks");
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    void addTest2(){
        Subtask s4 = new Subtask(null, false, 3, null, 0);
        s4.id = 4;
        ResponseEntity<Subtask> result = sut.add(s4);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void deleteTest(){
        ResponseEntity<Subtask> result = sut.deleteById(2);

        List<Subtask> expected = new ArrayList<>();
        Subtask test1 = new Subtask("Subtask 1", false, 0, null, 0);
        test1.id = 1;
        Subtask test3 = new Subtask("Subtask 3", false, 1, null, 0);
        test3.id = 3;
        expected.add(test1);
        expected.add(test3);
        List<Subtask> res = sut.getAll();
        assertEquals(ResponseEntity.ok(s2), result);
        assertEquals(expected, res);

        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add("/topic/subtasks");
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    void deleteTest2(){
        ResponseEntity<Subtask> result = sut.deleteById(12);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void updateStatusTest(){
        ResponseEntity<Subtask> result = sut.updateIndexById(2, "3");

        Subtask expected = new Subtask("Subtask 2", false, 3, null, 0);
        expected.id = 2;

        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add("/topic/subtasks");

        assertEquals(ResponseEntity.ok(expected), result);
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    void updateStatusTest2(){
        ResponseEntity<Subtask> result = sut.updateIndexById(12, "3");
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

}
