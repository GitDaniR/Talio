package server.services;

import commons.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.BoardListRepository;
import server.database.CardRepository;
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
        listRepo = null;
        c1 = new Card(0, "a", "a", 0, null);
        c2 = new Card(1, "b", "b", 1, null);
        c3 = new Card(2, "c", "c", 2, null);
        cards = new ArrayList<>();
        cards.add(c1);
        cards.add(c2);
        cards.add(c3);
        sut = new CardService(cardRepo, listRepo);
    }

    @Test
    public void testNotNull(){
        assertNotNull(sut);
    }

    @Test
    public void testFindAll(){
        cardRepo.setCards(cards);
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
        cardRepo.setCards(cards);
        Card res = sut.getCardById(1);
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(c2, res);
        expectedCalls.add(TestCardRepository.FIND_BY_ID);
        assertEquals(expectedCalls, cardRepo.getCalls());
    }

    @Test
    public void testFindByIdNotExists() throws Exception {
        cardRepo.setCards(cards);
        List<String> expectedCalls = new ArrayList<>();
        assertThrows(Exception.class, ()->sut.getCardById(4));
        expectedCalls.add(TestCardRepository.FIND_BY_ID);
        assertEquals(expectedCalls, cardRepo.getCalls());
    }
}
