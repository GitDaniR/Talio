package server.api;

import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.TestMessageChannel;
import server.database.TestSimpMessagingTemplate;
import server.database.TestTagRepository;
import server.services.TagService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TagControllerTest {
    private TestSimpMessagingTemplate simp;
    private TestTagRepository repo;
    private TagController sut;

    private Tag t1, t2, t3;
    List<Tag> tags;

    @BeforeEach
    void setup(){
        simp = new TestSimpMessagingTemplate(new TestMessageChannel());
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
        sut = new TagController(new TagService(repo), simp);
    }

    @Test
    public void constructorTest(){
        assertNotNull(sut);
    }

}
