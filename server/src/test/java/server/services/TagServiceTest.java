package server.services;

import commons.Subtask;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import server.database.TestTagRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TagServiceTest {
    private TestTagRepository repo;
    private Tag t1, t2, t3;
    List<Tag> tags;
    private TagService sut;

    @BeforeEach
    void setup(){
        repo = new TestTagRepository();
        t1 = new Tag("Tag 1", "0xff0000", null, 0);
        t1.id = 1;
        t2 = new Tag("Tag 2", "0x00ff00", null, 0);
        t2.id = 2;
        t3 = new Tag("Tag 3", "0x0000ff", null, 0);
        t3.id = 3;
        tags = new ArrayList<>();
        tags.add(t1);
        tags.add(t2);
        tags.add(t3);
        repo.setTags(tags);
        sut = new TagService(repo);
    }

    @Test
    public void constructorTest(){
        assertNotNull(sut);
    }

    @Test
    void getByIdTest() throws Exception {
        ResponseEntity<Tag> expected = ResponseEntity.ok(t2);
        ResponseEntity<Tag> result = sut.getById(2);
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestTagRepository.EXISTS_BY_ID);
        expectedCalls.add(TestTagRepository.FIND_BY_ID);
        assertEquals(expected, result);
        assertEquals(expectedCalls, repo.getCalls());
    }

    @Test
    void getByIdTest2(){
        assertThrows(Exception.class, ()->{sut.getById(12);});
    }

}
