package server.api;

import commons.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.database.BoardListRepository;
import server.database.TestBoardListRepository;
import server.database.TestCardRepository;
import server.services.CardService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardControllerTest {

    private TestCardRepository cardRepo;
    private TestBoardListRepository listRepo;
    private CardController sut;
    private Card c1, c2, c3;
    private List<Card> cards;

    @BeforeEach
    public void setup() {
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
        sut = new CardController(new CardService(cardRepo, listRepo));
    }

    @Test
    public void testGetAllCards(){
        List<Card> expected = new ArrayList<>();
        expected.add(new Card(0, "a", "a", 0, null, 1));
        expected.add(new Card(1, "b", "b", 1, null, 1));
        expected.add(new Card(2, "c", "c", 2, null, 1));
        List<Card> res = sut.getAllCards();
        assertEquals(expected, res);
    }

    @Test
    public void getCardById(){
        Card c = sut.getCardById(1).getBody();
        assertEquals(new Card(1, "b", "b", 1, null, 1), c);
    }

    @Test
    public void getCardByIdNotFound(){
        assertEquals(HttpStatus.NOT_FOUND, sut.getCardById(4).getStatusCode());
    }
}
