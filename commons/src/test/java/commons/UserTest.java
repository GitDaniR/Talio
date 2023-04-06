package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    public void emptyConstructorTest() {
        var user = new User();
        assertNotNull(user);
    }

    @Test
    public void hasBoardAlreadyTestTrue() {
        User u = new User("Sandi");
        u.boards = new ArrayList<>();
        u.boards.add(new Board("title","pass"));
        u.boards.get(0).id = 1;
        assertTrue(u.hasBoardAlready(1));
    }

    @Test
    public void hasBoardAlreadyTestFalse() {
        User user = new User("user");
        user.boards = new ArrayList<>();
        assertFalse(user.hasBoardAlready(2));
    }

    @Test
    public void equalsHashCode(){
        var user1 = new User("user");
        var user2 = new User("user");
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void notEqualsHashCode(){
        var user1 = new User("user 1");
        var user2 = new User("user 2");
        assertNotEquals(user1, user2);
        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void hasToString() {
        var user1 = new User("user 1");
        assertTrue(user1.toString().contains("user 1"));
    }
}