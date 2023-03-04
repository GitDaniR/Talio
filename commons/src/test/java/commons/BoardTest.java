package commons;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
public class BoardTest {

    @Test
    public void checkConstructor(){
        var board = new Board("Board", "password");
        assertEquals(board.title, "Board");
        assertEquals(board.password, "password");
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
