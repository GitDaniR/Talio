package server.services;

import commons.Board;
import commons.BoardList;
import commons.Card;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.TestBoardListRepository;
import server.database.TestBoardRepository;
import server.database.TestCardRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CardServiceTest {

    private TestCardRepository cardRepo;
    private TestBoardListRepository listRepo;
    private TestBoardRepository boardRepo;
    private CardService sut;
    private Board b1;
    private BoardList l1, l2;
    private Card c1, c2, c3;
    private List<Card> cards;

    @BeforeEach
    public void setup(){
        cardRepo = new TestCardRepository();
        listRepo = new TestBoardListRepository();
        boardRepo = new TestBoardRepository();
        b1 = new Board(0, "Main Board", "123", new ArrayList<>());
        boardRepo.save(b1);
        l1 = new BoardList(0, "First", b1, 0);
        l2 = new BoardList(1, "Second", b1, 0);
        listRepo.save(l1);
        listRepo.save(l2);
        c1 = new Card(0, "a", "a", 0, l1, 0);
        c2 = new Card(1, "b", "b", 1, l1, 0);
        c3 = new Card(2, "c", "c", 2, l1, 0);
        cards = new ArrayList<>();
        cards.add(c1);
        cards.add(c2);
        cards.add(c3);
        cardRepo.setCards(cards);
        sut = new CardService(cardRepo, listRepo);
    }

    @Test
    public void testNotNull(){
        assertNotNull(sut);
    }

    @Test
    public void testFindAll(){
        List<Card> res = sut.getAllCards();
        List<Card> expectedResult = new ArrayList<>();
        expectedResult.add(c1);
        expectedResult.add(c2);
        expectedResult.add(c3);
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestCardRepository.FIND_ALL);
        assertEquals(expectedResult, res);
        assertEquals(expectedCalls, cardRepo.getCalls());
    }

    @Test
    public void testFindById() throws Exception {
        Card res = sut.getCardById(1);
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(c2, res);
        expectedCalls.add(TestCardRepository.FIND_BY_ID);
        assertEquals(expectedCalls, cardRepo.getCalls());
    }

    @Test
    public void testFindByIdNotExists(){
        List<String> expectedCalls = new ArrayList<>();
        assertThrows(Exception.class, ()->sut.getCardById(4));
        expectedCalls.add(TestCardRepository.FIND_BY_ID);
        assertEquals(expectedCalls, cardRepo.getCalls());
    }

    @Test
    public void testAddCard() throws Exception {
        Card c4 = new Card(null, "d", "d", 1, l1, 0);
        Card res1 = sut.addCard(c4);
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestCardRepository.SHIFT_CARDS_RIGHT);
        expectedCalls.add(TestCardRepository.SAVE);
        assertEquals(expectedCalls, cardRepo.getCalls());
        Card res2 = sut.getCardById(1);
        assertEquals(c4, res1);
        assertEquals(new Card(3, "d", "d", 1, l1, 0), res1);
        assertEquals(new Card(1, "b", "b", 2, l1, 0), res2);
    }

    @Test
    public void testAddCardNoList(){
        Card c4 = new Card(5, "d", "d", 1, l1, 2);
        assertThrows(Exception.class, ()->sut.addCard(c4));
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, cardRepo.getCalls());
    }

    @Test
    public void testAddCardAlreadyExists(){
        Card c4 = new Card(2, "d", "d", 1, l1, 0);
        assertThrows(Exception.class, ()->sut.addCard(c4));
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestCardRepository.EXISTS_BY_ID);
        assertEquals(expectedCalls, cardRepo.getCalls());
    }

    @Test
    public void testDeleteById()throws Exception{
        Card res1 = sut.removeCardById(1);
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestCardRepository.FIND_BY_ID);
        expectedCalls.add(TestCardRepository.SHIFT_CARDS_LEFT);
        expectedCalls.add(TestCardRepository.DELETE_BY_ID);
        assertEquals(expectedCalls, cardRepo.getCalls());
        Card res2 = sut.getCardById(2);
        assertEquals(new Card(1, "b", "b", 1, l1, 0), res1);
        assertFalse(cardRepo.existsById(1));
        assertEquals(new Card(2, "c", "c", 1, l1, 0), res2);
    }

    @Test
    public void testDeleteByIdNotFound(){
        assertThrows(Exception.class, ()->sut.removeCardById(4));
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestCardRepository.FIND_BY_ID);
        assertEquals(expectedCalls, cardRepo.getCalls());
    }

    @Test
    public void testEditById() throws Exception {
        c2.title = "bb";
        c2.description = "bb";
        Card res = sut.editCardById(1, c2);
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestCardRepository.FIND_BY_ID);
        expectedCalls.add(TestCardRepository.SAVE);
        assertEquals(expectedCalls, cardRepo.getCalls());
        assertEquals(new Card(1, "bb", "bb", 1, l1, 0), res);
    }

    @Test
    public void testEditByIdNotFound() throws Exception {
        assertThrows(Exception.class, ()->sut.editCardById(4, c2));
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestCardRepository.FIND_BY_ID);
        assertEquals(expectedCalls, cardRepo.getCalls());
    }

    @Test
    public void testEditCardByIdList() throws Exception {
        Card res = sut.editCardByIdList(0, 1);
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestCardRepository.FIND_BY_ID);
        expectedCalls.add(TestCardRepository.SAVE);
        assertEquals(new Card(0, "a", "a", 0, l2, 1), res);
        assertEquals(expectedCalls, cardRepo.getCalls());
    }

    @Test
    public void testEditCardByIdListNotFoundCard() throws Exception {
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestCardRepository.FIND_BY_ID);
        assertThrows(Exception.class, () -> sut.editCardByIdList(100, 1));
        assertEquals(expectedCalls, cardRepo.getCalls());
    }

    @Test
    public void testEditCardByIdListNotFoundList() throws Exception {
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestCardRepository.FIND_BY_ID);
        assertThrows(Exception.class, () -> sut.editCardByIdList(1, 100));
        assertEquals(expectedCalls, cardRepo.getCalls());
    }

    @Test
    public void testRemoveTag() throws Exception {
        Tag tag = new Tag("title", null, b1, 0);
        c1.addTag(tag);
        Card res = sut.removeTag(0, tag);
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestCardRepository.FIND_BY_ID);
        expectedCalls.add(TestCardRepository.SAVE);
        assertEquals(new Card(0, "a", "a", 0, l1, 0), res);
        assertEquals(expectedCalls, cardRepo.getCalls());
    }

    @Test
    public void testRemoveTagNotFoundCard() throws Exception {
        Tag tag = new Tag("title", null, b1, 0);
        c1.addTag(tag);
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestCardRepository.FIND_BY_ID);
        assertThrows(Exception.class, () -> sut.removeTag(100, tag));
        assertEquals(expectedCalls, cardRepo.getCalls());
    }
}
