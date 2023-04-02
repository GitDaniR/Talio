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

    @Test
    void addTest() throws Exception {
        Tag t4 = new Tag("Tag 4", "0xff0000", null, 0);
        t4.id = 2;
        ResponseEntity<Tag> expected = ResponseEntity.ok(t4);

        ResponseEntity<Tag> result = sut.add(t4);

        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestTagRepository.SAVE);

        List<Tag> updated = new ArrayList<>();
        updated.add(t1);
        updated.add(t4);
        updated.add(t3);

        assertEquals(expected, result);
        assertEquals(expectedCalls, repo.getCalls());
        assertEquals(updated, repo.getTags());
    }

    @Test
    void addTest2(){
        Tag t4 = new Tag(null, "0xff0000", null, 0);
        t4.id = 2;
        assertThrows(Exception.class, ()->{sut.add(t4);});
    }

    @Test
    void deleteTest() throws Exception {
        ResponseEntity<Tag> expected = ResponseEntity.ok(t2);

        ResponseEntity<Tag> result = sut.deleteById(2);

        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestTagRepository.EXISTS_BY_ID);
        expectedCalls.add(TestTagRepository.FIND_BY_ID);
        expectedCalls.add(TestTagRepository.DELETE_BY_ID);

        List<Tag> updated = new ArrayList<>();

        Tag test1 = new Tag("Tag 1", "0xff0000", null, 0);
        test1.id = 1;
        Tag test2 = new Tag("Tag 3", "0x0000ff", null, 0);;
        test2.id = 3;
        updated.add(test1);
        updated.add(test2);

        assertEquals(expected, result);
        assertEquals(expectedCalls, repo.getCalls());
        assertEquals(updated, repo.getTags());
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
    void updateColorTest() throws Exception {
        Tag result = sut.updateColorById(2, "0x777777");

        Tag expected = new Tag("Tag 2", "0x777777", null, 0);
        expected.id = 2;

        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestTagRepository.FIND_BY_ID);
        expectedCalls.add(TestTagRepository.SAVE);


        assertEquals(expected, result);
        assertEquals(expectedCalls, repo.getCalls());
    }

    @Test
    void updateColorTest2(){
        assertThrows(Exception.class, ()->{sut.updateColorById(12, "0x777777");});
    }
    @Test
    void updateTitleTest() throws Exception {
        Tag result = sut.updateTitleById(2, "title");

        Tag expected = new Tag("title", "0x00ff00", null, 0);
        expected.id = 2;

        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestTagRepository.FIND_BY_ID);
        expectedCalls.add(TestTagRepository.SAVE);


        assertEquals(expected, result);
        assertEquals(expectedCalls, repo.getCalls());
    }

    @Test
    void updateTitleTest2(){
        assertThrows(Exception.class, ()->{sut.updateTitleById(12, "title");});
    }

}
