package commons;
//import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.sql.Array;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class CardTest {

    public static Board board;
    public static BoardList list;


    @BeforeEach
    public void setup(){
        board = new Board(1, "board", "password", new ArrayList<>());
        list = new BoardList(1,"list", board, 1);
        board.addBoardList(list);
    }

    @Test
    public void checkConstructor(){
        var card = new Card("card", "description", 0 , list, list.getId());
        assertEquals(card.title, "card");
        assertEquals(card.description, "description");
        assertEquals(card.list, list);
    }

    @Test
    public void checkConstructor2() {
        var card = new Card(1, "card", "description", 0, list, list.getId());
        assertEquals(card.title, "card");
        assertEquals(card.description, "description");
        assertEquals(card.list, list);
        assertEquals(card.listId, list.getId());
        assertEquals(card.index, 0);
    }

    @Test
    public void emptyConstructorTest() {
        var card = new Card();
        assertNotNull(card);
    }

    @Test
    public void getIdTest() {
        var card = new Card(1, "card", "description", 0, list, list.getId());
        assertEquals(card.getId(), 1);
    }

    @Test
    public void setPresetTest() {
        var card = new Card(1, "card", "description", 0, list, list.getId());
        var preset = new Preset(3, "red", "font", new ArrayList<>(),"name", board, board.id);
        card.setPreset(preset);
        assertEquals(card.preset, preset);
        assertEquals(card.presetId, 3);
    }

    @Test
    public void setIndexTest() {
        var card = new Card(1, "card", "description", 0, list, list.getId());
        card.setIndex(2);
        assertEquals(card.index, 2);
    }

    @Test
    public void setListTest() {
        var card = new Card(1, "card", "description", 0, null, 0);
        card.setList(list);
        assertEquals(card.list, list);
        assertEquals(card.listId, list.getId());
    }

    @Test
    public void addTagTest() {
        var card = new Card(1, "card", "description", 0, list, list.getId());
        var tag = new Tag("tag", "color");
        var listTags = new ArrayList<Tag>();
        listTags.add(tag);
        card.addTag(tag);
        assertEquals(card.tags, listTags);
    }

    @Test
    public void removeTagTest() {
        var card = new Card(1, "card", "description", 0, list, list.getId());
        var tag1 = new Tag("tag 1", "color");
        var tag2 = new Tag("tag 2", "color");
        var listTags = new ArrayList<Tag>();
        listTags.add(tag1);
        card.addTag(tag1);
        card.addTag(tag2);
        card.removeTag(tag2);
        assertTrue(card.tags.size() == 1);
        assertEquals(card.tags, listTags);
    }

    @Test
    public void equalsHashCode(){
        var card1 = new Card("card", "description", 0 ,list , null);
        var card2 = new Card("card", "description", 0 ,list , null);

        assertEquals(card1, card2);
        assertEquals(card1.hashCode(),card2.hashCode());
    }

    @Test
    public void notEqualsHashCode(){
        var card1 = new Card("card1", "description", 0 ,list , null);
        var card2 = new Card("card2", "description", 0 ,list , null);

        assertNotEquals(card1, card2);
        assertNotEquals(card1.hashCode(), card2.hashCode());

    }

    @Test
    public void hasToString(){
        var card1 = new Card("card", "description", 0 ,list, null );
        assertTrue(card1.toString().contains("card"));
        assertTrue(card1.toString().contains("description"));
        assertTrue(card1.toString().contains("0"));
    }


}
