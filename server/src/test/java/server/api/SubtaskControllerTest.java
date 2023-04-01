package server.api;

import commons.Card;
import commons.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.TestMessageChannel;
import server.database.TestSimpMessagingTemplate;
import server.database.TestSubtaskRepository;
import server.services.SubtaskService;

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
}
