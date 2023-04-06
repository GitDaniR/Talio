package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TagTest {

    @Test
    public void checkConstructor(){
        var tag = new Tag("Tag", "color");
        assertEquals(tag.title, "Tag");
        assertEquals(tag.colorBackground, "color");
        assertEquals(tag.cards, new ArrayList<Card>());
    }

    @Test
    public void checkConstructor2() {
        var board = new Board(1, "title", "password", new ArrayList<BoardList>());
        var tag = new Tag("tag", "color", board, 1);
        assertEquals(tag.title, "tag");
        assertEquals(tag.color, "color");
        assertEquals(tag.board, board);
        assertEquals(tag.boardId, 1);
    }

    @Test
    public void emptyConstructorTest() {
        var tag = new Tag();
        assertNotNull(tag);
    }

    @Test
    public void addToCard(){
        var tag = new Tag("Tag", "color");
        var card = new Card("TODO", "description", 0, null, null);
        var listCards = new ArrayList<Card>();
        listCards.add(card);
        tag.addToCard(card);
        assertEquals(tag.cards, listCards);
    }

    @Test
    public void removeFromCardTest() {
        var tag = new Tag("tag", "color");
        var card1 = new Card("card 1", "description", 0, null, null);
        var card2 = new Card("card 2", "description", 1, null, null);
        var listCards = new ArrayList<Card>();
        listCards.add(card2);
        tag.addToCard(card1);
        tag.addToCard(card2);
        tag.removeFromCard(card1);
        assertEquals(listCards, tag.cards);
        assertTrue(tag.cards.size() == 1);
    }


    @Test
    public void equalsHashCode(){
        var tag1 = new Tag("Tag", "color");
        var tag2 = new Tag("Tag", "color");
        assertEquals(tag1, tag2);
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void notEqualsHashCode(){

        var tag1 = new Tag("Tag1", "color");
        var tag2 = new Tag("Tag2", "color");
        assertNotEquals(tag1, tag2);
        assertNotEquals(tag1.hashCode(), tag2.hashCode());

    }

    @Test
    public void hasToString(){
        var tag1 = new Tag("Tag1", "color");
        assertTrue(tag1.toString().contains("Tag1"));
        assertTrue(tag1.toString().contains("color"));

    }

}
