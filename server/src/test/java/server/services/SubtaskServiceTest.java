package server.services;

import commons.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import server.database.SubtaskRepository;
import server.database.TestCardRepository;
import server.database.TestSubtaskRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SubtaskServiceTest {

    private TestSubtaskRepository repo;
    private Subtask s1, s2, s3;
    List<Subtask> subtasks;
    private SubtaskService sut;

    @BeforeEach
    void setup(){
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
        sut = new SubtaskService(repo);
    }

    @Test
    void constructorTest(){
        assertNotNull(sut);
    }

    @Test
    void getByIdTest() throws Exception {
        ResponseEntity<Subtask> expected = ResponseEntity.ok(s2);
        ResponseEntity<Subtask> result = sut.getById(2);
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestCardRepository.EXISTS_BY_ID);
        expectedCalls.add(TestCardRepository.FIND_BY_ID);
        assertEquals(expected, result);
        assertEquals(expectedCalls, repo.getCalls());
    }

    @Test
    void getByIdTest2(){
        assertThrows(Exception.class, ()->{sut.getById(12);});
    }
}
