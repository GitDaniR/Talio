package commons;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    public void checkConstructor(){
        var board = new Board("Board", "password");
        assertEquals(board.title, "Board");
        assertEquals(board.password, "password");
    }

    @Test
    public void checkConstructor2() {
        List<BoardList> lists = new ArrayList<>();
        var board = new Board(1, "Board", "password", lists);
        assertNotNull(board);
        assertEquals(board.id, 1);
        assertEquals(board.title, "Board");
        assertEquals(board.password, "password");
        assertTrue(lists.equals(board.lists));
    }

    @Test
    public void addBoardListTest() {
        List<BoardList> lists = new ArrayList<>();
        var board = new Board(1, "Board", "password", lists);
        var boardList = new BoardList("List", board, 1);
        board.addBoardList(boardList);
        List<BoardList> expected = new ArrayList<>();
        expected.add(boardList);
        assertTrue(expected.equals(board.lists));
    }

    @Test
    public void checkEmptyConstructor() {
        var board = new Board();
        assertNotNull(board);
    }

    @Test
    public void checkSetLists(){
        var lists = new ArrayList<BoardList>();
        var board = new Board("Board", "password");
        board.setLists(lists);
        assertEquals(lists, board.lists);
    }

    @Test
    public void equalsHashCode(){
        var board1 = new Board("Board", "password");
        var board2 = new Board("Board", "password");

        assertEquals(board1, board2);
        assertEquals(board1.hashCode(), board2.hashCode());
    }

    @Test
    public void notEqualsHashCode(){
        var board1 = new Board("Board", "password1");
        var board2 = new Board("Board", "password2");

        assertNotEquals(board1, board2);
        assertNotEquals(board1.hashCode(), board2.hashCode());

    }

    @Test
    public void hasToString(){
        var board1 = new Board("Board", "password");
        assertTrue(board1.toString().contains("Board"));
        assertTrue(board1.toString().contains("password"));

    }
}
