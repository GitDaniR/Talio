package server.api;

import commons.Board;
import commons.BoardList;
import commons.Card;
import commons.Preset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.database.TestBoardListRepository;
import server.database.TestBoardRepository;
import server.database.TestCardRepository;
import server.database.TestPresetRepository;
import server.services.CardService;
import server.services.PresetService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PresetControllerTest {

    private TestPresetRepository repo;
    private TestBoardRepository boardRepo;
    private List<Preset> presets;
    private Board board;

    private PresetController sut;

    private List<Card> cards;

    @BeforeEach
    public void setup(){
        TestCardRepository cardRepo;
        boardRepo = new TestBoardRepository();
        cardRepo = new TestCardRepository();
        repo = new TestPresetRepository();
        List<Board> boards = new ArrayList<>();
        board = new Board(0, "Main Board", "123", new ArrayList<>());
        boards.add(board);
        boardRepo.setBoards(boards);
        presets = new ArrayList<>();
        Preset p1 = new Preset(0,"background1","font1", board);
        Preset p2 = new Preset(1, "background2","font2", board);
        Preset p3 = new Preset(2, "background3","font3", board);

        presets.add(p1);
        presets.add(p2);
        presets.add(p3);

        repo.setPresets(presets);
        BoardList l1 = new BoardList(0, "First", board, 0);

        cards = new ArrayList<>();
        Card card1 = new Card(0, "a", "a", 0, l1, 0);
        cards.add(card1);
        cardRepo.setCards(cards);

        sut = new PresetController(new PresetService(repo, boardRepo));
    }

    @Test
    public void testNotNull(){
        assertNotNull(sut);
    }

    @Test
    public void testFindAll(){
        List<Preset> res = sut.getAll();
        List<Preset> expectedPresets = new ArrayList<>();
        Preset p1 = new Preset(0,"background1","font1", board);
        Preset p2 = new Preset(1, "background2","font2", board);
        Preset p3 = new Preset(2, "background3","font3", board);

        expectedPresets.add(p1);
        expectedPresets.add(p2);
        expectedPresets.add(p3);

        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(repo.FIND_ALL);
        assertEquals(expectedPresets, res);
        assertEquals(expectedCalls, repo.getCalls());
    }

    @Test
    public void testGetCardById(){
        Preset p1 = new Preset(0,"background1","font1", board);
        ResponseEntity<Preset> response = sut.getById(0);
        assertEquals(ResponseEntity.ok(p1), response);
    }

    @Test
    public void testGetCardByIdNotFound(){
        assertEquals(HttpStatus.NOT_FOUND, sut.getById(4).getStatusCode());
    }

    @Test
    public void testAddPreset(){
        Preset newPreset = new Preset(3, "b", "f", board);
        ResponseEntity<Preset>  response = sut.add(newPreset);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(newPreset, response.getBody());
    }

    @Test
    public void testAddPresetBadRequest(){
        assertEquals(HttpStatus.BAD_REQUEST, sut.add(new Preset(1, "b", "f", board)).getStatusCode());
    }

    @Test
    public void testDeletePreset(){

        List<Preset> expectedPresets = new ArrayList<>();
        Preset p1 = new Preset(0,"background1","font1", board);
        Preset p2 = new Preset(1, "background2","font2", board);
        Preset p3 = new Preset(2, "background3","font3", board);

        expectedPresets.add(p1);
        expectedPresets.add(p3);
        Preset expectedPreset  = p2;

        ResponseEntity<Preset> response = sut.deleteById(1);
        assertEquals(expectedPreset, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(expectedPresets, repo.getPresets());
    }

    @Test
    public void testDeletePresetNotFound(){
        assertEquals(HttpStatus.NOT_FOUND, sut.deleteById(4).getStatusCode());
    }


    @Test
    public void testDeletePresetPersistenceException() throws Exception {
        Preset p4 = new Preset(3,"background1","font1", board);
        p4.cards = cards;
        sut.add(p4);
        assertEquals(HttpStatus.BAD_REQUEST, sut.deleteById(p4.id).getStatusCode());

    }
    @Test
    public void testUpdateBackgroundById(){
        Preset newPreset =  new Preset(0,"color","font1", board);
        ResponseEntity<String> response = sut.updateBackgroundById(0, "color");
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(newPreset,sut.getAll().get(0));
    }

    @Test
    public void testUpdateBackgroundByIdBadRequest(){
        ResponseEntity<String> response = sut.updateBackgroundById(10, "color");
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
    }

    @Test
    public void testUpdateFontById(){
        Preset newPreset =  new Preset(0,"color","font1", board);
        ResponseEntity<String> response = sut.updateBackgroundById(0, "color");
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(newPreset,sut.getAll().get(0));
    }

    @Test
    public void testUpdateFontByIdBadRequest(){
        ResponseEntity<String> response = sut.updateBackgroundById(10, "color");
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
    }

    @Test
    public void testSetDefault(){
        ResponseEntity<Preset> response = sut.setDefault(0);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(presets.get(0), response.getBody());

    }
    @Test
    public void testSetDefaultBadRequest(){
        ResponseEntity<Preset> response = sut.setDefault(10);
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());

    }









}
