package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void hasBoardAlready() {
        User u = new User("Sandi");
        u.boards = new ArrayList<>();
        u.boards.add(new Board("title","pass"));
        u.boards.get(0).id = 1;
        assertTrue(u.hasBoardAlready(1));
    }
}