package server.api;

import commons.Board;
import commons.BoardList;
import commons.Card;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.database.*;
import server.services.CardService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardControllerTest {

    private TestSimpMessagingTemplate simp;
    private TestCardRepository cardRepo;
    private TestBoardListRepository listRepo;

    private TestBoardRepository boardRepo;
    private CardController sut;
    private Board b1;
    private BoardList l1;
    private Card c1, c2, c3;
    private List<Card> cards;

    @BeforeEach
    public void setup() {
        simp = new TestSimpMessagingTemplate(new TestMessageChannel());
        cardRepo = new TestCardRepository();
        listRepo = new TestBoardListRepository();
        boardRepo = new TestBoardRepository();
        b1 = new Board(0, "Main Board", "123", new ArrayList<>());
        boardRepo.save(b1);
        l1 = new BoardList(0, "First", b1, 0);
        listRepo.save(l1);
        c1 = new Card(0, "a", "a", 0, l1, 0);
        c2 = new Card(1, "b", "b", 1, l1, 0);
        c3 = new Card(2, "c", "c", 2, l1, 0);
        cards = new ArrayList<>();
        cards.add(c1);
        cards.add(c2);
        cards.add(c3);
        cardRepo.setCards(cards);
        sut = new CardController(new CardService(cardRepo, listRepo), simp);
    }

    @Test
    public void testGetAllCards(){
        List<Card> expected = new ArrayList<>();
        expected.add(new Card(0, "a", "a", 0, l1, 0));
        expected.add(new Card(1, "b", "b", 1, l1, 0));
        expected.add(new Card(2, "c", "c", 2, l1, 0));
        List<Card> res = sut.getAllCards();
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expected, res);
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    public void testGetCardById(){
        ResponseEntity<Card> cardResponse = sut.getCardById(1);
        assertEquals(HttpStatus.OK, cardResponse.getStatusCode());
        assertEquals(new Card(1, "b", "b", 1, l1, 0), cardResponse.getBody());
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    public void testGetCardByIdNotFound(){
        assertEquals(HttpStatus.NOT_FOUND, sut.getCardById(4).getStatusCode());
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    public void testAddCard(){
        ResponseEntity<Card>  cardResponse = sut.addCard(new Card(3, "d", "d", 3, l1, 0));
        List<Card> expected = new ArrayList<>();
        expected.add(new Card(0, "a", "a", 0, l1, 0));
        expected.add(new Card(1, "b", "b", 1, l1, 0));
        expected.add(new Card(2, "c", "c", 2, l1, 0));
        expected.add(new Card(3, "d", "d", 3, l1, 0));
        assertEquals(HttpStatus.OK, cardResponse.getStatusCode());
        assertEquals(new Card(3, "d", "d", 3, l1, 0), cardResponse.getBody());
        assertEquals(expected, cardRepo.getCards());
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add("/topic/cards");
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    public void testAddCardBadRequest(){
        assertEquals(HttpStatus.BAD_REQUEST, sut.addCard(new Card(2, "d", "d", 3, l1, 0)).getStatusCode());
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    public void testDeleteCard(){
        ResponseEntity<Card> cardResponse = sut.removeCard(1);
        List<Card> expected = new ArrayList<>();
        expected.add(new Card(0, "a", "a", 0, l1, 0));
        expected.add(new Card(2, "c", "c", 1, l1, 0));
        assertEquals(HttpStatus.OK, cardResponse.getStatusCode());
        assertEquals(new Card(1, "b", "b", 1, l1, 0), cardResponse.getBody());
        assertEquals(expected, cardRepo.getCards());
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add("/topic/cards");
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    public void testDeleteCardNotFound(){
        assertEquals(HttpStatus.NOT_FOUND, sut.removeCard(4).getStatusCode());
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    public void testEditCard(){
        ResponseEntity<Card> cardResponse = sut.editCard(1, new Card(2, "d", "d", 3, l1, 0));
        List<Card> expected = new ArrayList<>();
        expected.add(new Card(0, "a", "a", 0, l1, 0));
        expected.add(new Card(2, "c", "c", 2, l1, 0));
        expected.add(new Card(1, "d", "d", 1, l1, 0));
        assertEquals(HttpStatus.OK, cardResponse.getStatusCode());
        assertEquals(new Card(1, "d", "d", 1, l1, 0), cardResponse.getBody());
        assertEquals(expected, cardRepo.getCards());
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add("/topic/cards/rename");
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    public void testEditCardNotFound(){
        assertEquals(HttpStatus.NOT_FOUND, sut.editCard(3, new Card(3, "d", "d", 3, l1, 0)).getStatusCode());
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    public void testEditCardList() {
        BoardList l2 = new BoardList(1, "Second", b1, 0);
        listRepo.save(l2);
        ResponseEntity<Card> cardResponse = sut.editCardList(1, 1, 1);
        assertEquals(HttpStatus.OK, cardResponse.getStatusCode());
        assertEquals(new Card(1, "b", "b", 1, l2, 1), cardResponse.getBody());
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add("/topic/cards/edit");
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    public void testEditCardListNotFound() {
        ResponseEntity<Card> cardResponse = sut.editCardList(1, 2, 1);
        assertEquals(HttpStatus.NOT_FOUND, cardResponse.getStatusCode());
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    public void testRemoveTagFromCard() {
        Tag tag = new Tag("title", null, b1,0);
        c2.addTag(tag);
        ResponseEntity<Card> cardResponse = sut.removeTagFromCard(1, tag);
        assertEquals(HttpStatus.OK, cardResponse.getStatusCode());
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add("/topic/cards/remove/tag");
        assertEquals(expectedCalls, simp.getDestinations());
    }

    @Test
    public void testRemoveTagFromCardNotFound() {
        ResponseEntity<Card> cardResponse = sut.removeTagFromCard(4, null);
        assertEquals(HttpStatus.NOT_FOUND, cardResponse.getStatusCode());
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, simp.getDestinations());
    }
}
