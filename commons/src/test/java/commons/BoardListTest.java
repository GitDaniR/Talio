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
        var list = new BoardList("List", board, 1);
        assertEquals(list.board, board);
        assertEquals(list.title, "List");
    }

    @Test
    public void checkConstructor2() {
        var list = new BoardList(2,"List", board, 1);
        assertEquals(list.board, board);
        assertEquals(list.title, "List");
        assertEquals(list.id, 2);
        assertEquals(list.boardId, 1);
        assertNotNull(list);
    }

    @Test
    public void checkEmptyConstructor() {
        var list = new BoardList();
        assertNotNull(list);
    }

    @Test
    public void checkOnlyTitleContructor() {
        var list = new BoardList("list");
        assertNotNull(list);
        assertEquals(list.title, "list");
    }

    @Test
    public void getIdTest() {
        var list = new BoardList(1,"List", board, 1);
        assertEquals(1, list.getId());
    }

    @Test
    public void getCardByIndexTest() {
        var list = new BoardList(1,"List", board, 1);
        var newCard = new Card("Card", "Description", 0, list, 1);
        list.addCard(newCard);
        assertTrue(newCard.equals(list.getCardByIndex(0)));
    }

    @Test
    public void checkSetCards(){
        var list = new BoardList("List", board, 1);
        var cards = new ArrayList<Card>();
        list.setCards(cards);
        assertEquals(list.cards, cards);
    }

    @Test
    public void checkAddCard(){
        var list = new BoardList("List", board, 1);
        var card = new Card("title", "description", 1, null,null);
        var cards = new ArrayList<Card>();
        cards.add(card);
        list.addCard(card);
        assertEquals(list.cards, cards);

    }

    @Test
    public void equalsHashCode(){
        var list1 = new BoardList("List1", board, 1);
        var list2 = new BoardList("List1", board, 1);

        assertEquals(list1, list2);
        assertEquals(list1.hashCode(), list2.hashCode());
    }

    @Test
    public void notEqualsHashCode(){
        var list1 = new BoardList("List1", board, 1);
        var list2 = new BoardList("List2", board, 1);

        assertNotEquals(list1, list2);
        assertNotEquals(list1.hashCode(), list2.hashCode());

    }

    @Test
    public void hasToString(){
        var list = new BoardList("List1", board, 1);
        assertTrue(list.toString().contains("List1"));

    }
}
