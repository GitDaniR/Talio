package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class BoardListTest {

    private static Board board;

    @BeforeEach
    public void setup(){
        board = new Board("Board", "password");
    }

    @Test
    public void checkConstructor(){
        var list = new BoardList("List", board);
        assertEquals(list.board, board);
        assertEquals(list.title, "List");
    }

    @Test
    public void checkSetCards(){
        var list = new BoardList("List", board);
        var cards = new ArrayList<Card>();
        list.setCards(cards);
        assertEquals(list.cards, cards);
    }

    @Test
    public void checkAddCard(){
        var list = new BoardList("List", board);
        var card = new Card("title", "description", 1, null,null);
        var cards = new ArrayList<Card>();
        cards.add(card);
        list.addCard(card);
        assertEquals(list.cards, cards);

    }

    @Test
    public void equalsHashCode(){
        var list1 = new BoardList("List1", board);
        var list2 = new BoardList("List1", board);

        assertEquals(list1, list2);
        assertEquals(list1.hashCode(), list2.hashCode());
    }

    @Test
    public void notEqualsHashCode(){
        var list1 = new BoardList("List1", board);
        var list2 = new BoardList("List2", board);

        assertNotEquals(list1, list2);
        assertNotEquals(list1.hashCode(), list2.hashCode());

    }

    @Test
    public void hasToString(){
        var list = new BoardList("List1", board);
        assertTrue(list.toString().contains("List1"));

    }
}
