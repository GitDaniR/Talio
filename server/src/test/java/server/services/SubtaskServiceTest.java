package server.services;

import commons.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.SubtaskRepository;
import server.database.TestSubtaskRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SubtaskServiceTest {

    private TestSubtaskRepository repo;
    private Subtask s1, s2, s3;
    List<Subtask> subtasks;
    private SubtaskService sut;

    @BeforeEach
    void setup(){
        repo = new TestSubtaskRepository();
        s1 = new Subtask("Subtask 1", false, 0, null, 0);
        s2 = new Subtask("Subtask 2", false, 1, null, 0);
        s3 = new Subtask("Subtask 3", false, 2, null, 0);
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
}
