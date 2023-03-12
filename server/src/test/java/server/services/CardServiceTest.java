package server.services;

import commons.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.BoardListRepository;
import server.database.CardRepository;
import server.database.TestBoardListRepository;
import server.database.TestCardRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CardServiceTest {

    private TestCardRepository cardRepo;
    private BoardListRepository listRepo;
    private CardService sut;
    private Card c1, c2, c3;
    private List<Card> cards;

    @BeforeEach
    public void setup(){
        cardRepo = new TestCardRepository();
        listRepo = new TestBoardListRepository();
        c1 = new Card(0, "a", "a", 0, null, 1);
        c2 = new Card(1, "b", "b", 1, null, 1);
        c3 = new Card(2, "c", "c", 2, null, 1);
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
        Card c4 = new Card(null, "d", "d", 1, null, 1);
        Card res1 = sut.addCard(c4);
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestCardRepository.SHIFT_CARDS_RIGHT);
        expectedCalls.add(TestCardRepository.SAVE);
        assertEquals(expectedCalls, cardRepo.getCalls());
        Card res2 = sut.getCardById(1);
        assertEquals(c4, res1);
        assertEquals(new Card(3, "d", "d", 1, null, 1), res1);
        assertEquals(new Card(1, "b", "b", 2, null, 1), res2);
    }

    @Test
    public void testAddCardNoList(){
        Card c4 = new Card(5, "d", "d", 1, null, 2);
        assertThrows(Exception.class, ()->sut.addCard(c4));
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, cardRepo.getCalls());
    }

    @Test
    public void testAddCardAlreadyExists(){
        Card c4 = new Card(2, "d", "d", 1, null, 1);
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
        assertEquals(new Card(1, "b", "b", 1, null, 1), res1);
        assertFalse(cardRepo.existsById(1));
        assertEquals(new Card(2, "c", "c", 1, null, 1), res2);
    }

    @Test
    public void testDeleteByIdNotFound(){
        assertThrows(Exception.class, ()->sut.removeCardById(4));
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestCardRepository.FIND_BY_ID);
        assertEquals(expectedCalls, cardRepo.getCalls());
    }

}
