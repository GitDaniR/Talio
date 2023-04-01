package server.services;

import commons.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
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
        expectedCalls.add(TestSubtaskRepository.EXISTS_BY_ID);
        expectedCalls.add(TestSubtaskRepository.FIND_BY_ID);
        assertEquals(expected, result);
        assertEquals(expectedCalls, repo.getCalls());
    }

    @Test
    void getByIdTest2(){
        assertThrows(Exception.class, ()->{sut.getById(12);});
    }

    @Test
    void addTest() throws Exception {
        Subtask s4 = new Subtask("Subtask 4", false, 3, null, 0);
        s4.id = 2;
        ResponseEntity<Subtask> expected = ResponseEntity.ok(s4);

        ResponseEntity<Subtask> result = sut.add(s4);

        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestSubtaskRepository.SAVE);

        List<Subtask> updated = new ArrayList<>();
        updated.add(s1);
        updated.add(s4);
        updated.add(s3);

        assertEquals(expected, result);
        assertEquals(expectedCalls, repo.getCalls());
        assertEquals(updated, repo.getSubtasks());
    }

    @Test
    void addTest2(){
        Subtask s4 = new Subtask(null, false, 3, null, 0);
        s4.id = 2;
        assertThrows(Exception.class, ()->{sut.add(s4);});
    }

    @Test
    void deleteTest() throws Exception {
        ResponseEntity<Subtask> expected = ResponseEntity.ok(s2);

        ResponseEntity<Subtask> result = sut.deleteById(2);

        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestSubtaskRepository.EXISTS_BY_ID);
        expectedCalls.add(TestSubtaskRepository.FIND_BY_ID);
        expectedCalls.add(TestSubtaskRepository.SHIFT_DOWN);
        expectedCalls.add(TestSubtaskRepository.DELETE_BY_ID);

        List<Subtask> updated = new ArrayList<>();
        updated.add(s1);
        updated.add(s3);

        assertEquals(expected, result);
        assertEquals(expectedCalls, repo.getCalls());
        assertEquals(updated, repo.getSubtasks());
    }

    @Test
    void deleteTest2(){
        assertThrows(Exception.class, ()->{sut.deleteById(12);});
    }

    @Test
    void getRepoTest(){
        assertEquals(repo, sut.getRepo());
    }

    @Test
    void updateStatusTest() throws Exception {
        Subtask result = sut.updateSubtaskStatus(2, "true");

        Subtask expected = new Subtask("Subtask 2", true, 1, null, 0);
        expected.id = 2;

        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestSubtaskRepository.FIND_BY_ID);
        expectedCalls.add(TestSubtaskRepository.SAVE);


        assertEquals(expected, result);
        assertEquals(expectedCalls, repo.getCalls());
    }

    @Test
    void updateStatusTest2(){
        assertThrows(Exception.class, ()->{sut.updateSubtaskStatus(12, "false");});
    }

    @Test
    void updateIndex() throws Exception {
        Subtask result = sut.updateIndexById(2, "3");

        Subtask expected = new Subtask("Subtask 2", false, 3, null, 0);
        expected.id = 2;

        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestSubtaskRepository.FIND_BY_ID);
        expectedCalls.add(TestSubtaskRepository.SAVE);


        assertEquals(expected, result);
        assertEquals(expectedCalls, repo.getCalls());
    }

    @Test
    void updateIndex2(){
        assertThrows(Exception.class, ()->{sut.updateIndexById(12, "18");});
    }

    @Test
    void updateTitle() throws Exception {
        Subtask result = sut.updateTitleById(2, "title");

        Subtask expected = new Subtask("title", false, 1, null, 0);
        expected.id = 2;

        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestSubtaskRepository.FIND_BY_ID);
        expectedCalls.add(TestSubtaskRepository.SAVE);


        assertEquals(expected, result);
        assertEquals(expectedCalls, repo.getCalls());
    }

    @Test
    void updateTitle2(){
        assertThrows(Exception.class, ()->{sut.updateTitleById(12, "title");});
    }

}
