package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PresetTest {

    public static Board board;
    public static List<Card> cards;

    @BeforeEach
    public void setup() {
        board = new Board(1, "title", "password", new ArrayList<>());
        cards = new ArrayList<>();
    }

    @Test
    public void constructorTest() {
        Preset preset = new Preset("red", "font", cards, board, board.id);
        assertNotNull(preset);
        assertEquals(preset.backgroundColor, "red");
        assertEquals(preset.font, "font");
        assertEquals(preset.cards, cards);
        assertEquals(preset.board, board);
        assertEquals(preset.boardId, 1);
    }

    @Test
    public void constructorTest2() {
        Preset preset = new Preset(3, "red", "font", cards, board, board.id);
        assertNotNull(preset);
        assertEquals(preset.id, 3);
        assertEquals(preset.backgroundColor, "red");
        assertEquals(preset.font, "font");
        assertEquals(preset.cards, cards);
        assertEquals(preset.board, board);
        assertEquals(preset.boardId, 1);
    }

    @Test
    public void constructorTest3() {
        Preset preset = new Preset(3, "red", "font", cards,"name", board, board.id);
        assertNotNull(preset);
        assertEquals(preset.id, 3);
        assertEquals(preset.backgroundColor, "red");
        assertEquals(preset.font, "font");
        assertEquals(preset.cards, cards);
        assertEquals(preset.name, "name");
        assertEquals(preset.board, board);
        assertEquals(preset.boardId, 1);
    }

    @Test
    public void constructorTest4() {
        Preset preset = new Preset("red", "font", cards, "name", board, board.id);
        assertNotNull(preset);
        assertEquals(preset.backgroundColor, "red");
        assertEquals(preset.font, "font");
        assertEquals(preset.cards, cards);
        assertEquals(preset.name, "name");
        assertEquals(preset.board, board);
        assertEquals(preset.boardId, 1);
    }

    @Test
    public void emptyConstructorTest() {
        Preset preset = new Preset();
        assertNotNull(preset);
    }

    @Test
    public void getIdTest() {
        Preset preset = new Preset(3, "red", "font", cards,"name", board, board.id);
        assertEquals(preset.getId(), 3);
    }

    @Test
    public void getNameTest() {
        Preset preset = new Preset(3, "red", "font", cards,"name", board, board.id);
        assertEquals(preset.getName(), "name");
    }

    @Test
    public void setBackgroundColorTest() {
        Preset preset = new Preset(3, "red", "font", cards,"name", board, board.id);
        preset.setBackgroundColor("blue");
        assertEquals(preset.backgroundColor, "blue");
    }

    @Test
    public void setFontTest() {
        Preset preset = new Preset(3, "red", "font", cards,"name", board, board.id);
        preset.setFont("another font");
        assertEquals(preset.font, "another font");
    }

    @Test
    public void setBoardTest() {
        Preset preset = new Preset(3, "red", "font", cards,"name", board, board.id);
        Board newBoard = new Board(5, "new board", "password", new ArrayList<>());
        preset.setBoard(newBoard);
        assertEquals(preset.board, newBoard);
        assertEquals(preset.boardId, 5);
    }

    @Test
    public void addCardAlreadyContainedTest() {
        Preset preset = new Preset(3, "red", "font", cards,"name", board, board.id);
        Card newCard = new Card("title", "description", 1, new BoardList(), 1);
        cards.add(newCard);
        preset.addCard(newCard);
        assertTrue(preset.cards.contains(newCard));
        assertTrue(preset.cards.size() == 1);
    }

    @Test
    public void addCardNotContainedTest() {
        Preset preset = new Preset(3, "red", "font", cards,"name", board, board.id);
        Card newCard = new Card("title", "description", 1, new BoardList(), 1);
        assertFalse(preset.cards.contains(newCard));
        assertTrue(preset.cards.size() == 0);
        preset.addCard(newCard);
        assertTrue(preset.cards.contains(newCard));
        assertTrue(preset.cards.size() == 1);
    }

    @Test
    public void removeCardTest() {
        Preset preset = new Preset(3, "red", "font", cards,"name", board, board.id);
        Card newCard = new Card("title", "description", 1, new BoardList(), 1);
        preset.addCard(newCard);
        assertTrue(preset.cards.contains(newCard));
        assertTrue(preset.cards.size() == 1);
        preset.removeCard(newCard);
        assertFalse(preset.cards.contains(newCard));
        assertTrue(preset.cards.size() == 0);
    }

    @Test
    public void equalsHashCodeTest() {
        Preset preset1 = new Preset(3, "red", "font", cards,"name", board, board.id);
        Preset preset2 = new Preset(3, "red", "font", cards,"name", board, board.id);

        assertEquals(preset1, preset2);
        assertEquals(preset1.hashCode(), preset2.hashCode());
        assertEquals(preset1.hashCode(), preset1.hashCode());
    }

    @Test
    public void toStringTest() {
        Preset preset = new Preset(3, "red", "font", cards,"name", board, board.id);
        String result = preset.toString();

        assertTrue(result.equals("name"));
    }
}
