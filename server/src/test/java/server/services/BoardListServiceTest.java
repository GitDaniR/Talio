package server.services;

import commons.BoardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.TestBoardListRepository;
import server.database.TestBoardRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardListServiceTest {

    private TestBoardListRepository listRepo;
    private TestBoardRepository boardRepo;
    private BoardListService sut;
    private BoardList l1, l2, l3;
    private List<BoardList> boardLists;

    @BeforeEach
    void setUp() {
        listRepo = new TestBoardListRepository();
        boardRepo = new TestBoardRepository();
        l1 = new BoardList("First");
        l2 = new BoardList("Second");
        l3 = new BoardList("Third");
        boardLists = new ArrayList<>();
        boardLists.add(l1);
        boardLists.add(l2);
        boardLists.add(l3);
        listRepo.setBoardLists(boardLists);
        sut = new BoardListService(listRepo, boardRepo);
    }

    @Test
    public void constructorTest() {
        assertNotNull(sut);
    }

    @Test
    void findAll() {
    }

    @Test
    void add() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void updateTitleById() {
    }
}