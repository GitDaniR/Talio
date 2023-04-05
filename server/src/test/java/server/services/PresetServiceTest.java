package server.services;

import commons.Board;
import commons.BoardList;
import commons.Card;
import commons.Preset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.TestBoardRepository;
import server.database.TestPresetRepository;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PresetServiceTest {

    private PresetService sut;

    private TestPresetRepository repo;
    private TestBoardRepository boardRepo;
    private List<Preset> presets;
    private Board board;


    @BeforeEach
    public void setup(){
        boardRepo = new TestBoardRepository();
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

        sut = new PresetService(repo, boardRepo);

    }
    @Test
    public void constructorTest(){assertNotNull(sut);}
    @Test
    public void testGetAllPresets(){
        List<Preset> results = sut.getAllPresets();
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestPresetRepository.FIND_ALL);
        assertEquals(repo.getCalls(), expectedCalls);

        assertEquals(presets, results);
    }

    @Test
    public void testGetById() throws Exception {
        Preset result = sut.getById(0);
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestPresetRepository.FIND_BY_ID);
        assertEquals(expectedCalls, repo.getCalls());
        assertEquals(presets.get(0), result);

    }
    @Test
    public void testGetByIdException() throws Exception{
        assertThrows(Exception.class, () -> sut.getById(5));
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(repo.FIND_BY_ID);
        assertEquals(expectedCalls, repo.getCalls());
    }

    @Test
    public void  testAdd() throws Exception {
        Preset p4 = new Preset(3, "background4","font4", board);
        sut.add(p4);
        List<String> expectedCals = new ArrayList<>();
        expectedCals.add(repo.EXISTS_BY_ID);
        expectedCals.add(repo.SAVE);
        assertEquals(expectedCals, repo.getCalls());

    }

    @Test
    public void testAddNonValidBoard() throws Exception{
        Preset p4 = new Preset(3, "background4","font4", null);
        assertThrows(Exception.class, () -> sut.add(p4));
        List<String> expectedCals = new ArrayList<>();
        assertEquals(expectedCals, repo.getCalls());
    }

    @Test
    public void testAddNonValidId(){
        Preset p4 = new Preset(0, "background4","font4", null);
        assertThrows(Exception.class, () -> sut.add(p4));
        List<String> expectedCals = new ArrayList<>();
        assertEquals(expectedCals, repo.getCalls());
    }
    @Test
    public void testDeleteById() throws Exception {
        Preset deleted = sut.deleteById(0);
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(repo.FIND_BY_ID);
        expectedCalls.add(repo.DELETE_BY_ID);
        assertEquals(expectedCalls, repo.getCalls());
        assertTrue(!repo.presets.contains(deleted));

    }

    @Test
    public void testDeleteByIdNonValid() throws Exception {
        Preset  p4 = new Preset(3,"background1","font1", board);
        p4.cards.add(new Card());
        sut.add(p4);
        presets.add(p4);
        assertThrows(PersistenceException.class, () -> sut.deleteById(3));
        assertEquals(presets, sut.getAllPresets());
    }

    @Test
    public void testDeleteByIdNotFound() throws Exception {
        assertThrows(Exception.class, () -> sut.deleteById(10));
        assertEquals(presets, sut.getAllPresets());
    }

    @Test
    public void testEditPresetBackgroundById() throws Exception {
        String color = "color";
        sut.editPresetBackgroundById(0, color);
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(repo.EXISTS_BY_ID);
        expectedCalls.add(repo.UPDATE_BACKGROUND_BY_ID);
        assertEquals(expectedCalls, repo.getCalls());
        assertEquals(color, sut.getById(0).backgroundColor);

    }

    @Test
    public void testEditPresetBackgroundByIdNonValidId() throws Exception {
        String color = "color";
        assertThrows( Exception.class, () -> sut.editPresetBackgroundById(-1, color));
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, repo.getCalls());
        assertEquals(presets.get(0).backgroundColor, sut.getById(0).backgroundColor);

    }

    @Test
    public void testEditPresetBackgroundByIdNonExistent(){
        assertThrows( Exception.class, () -> sut.editPresetBackgroundById(8, "color"));
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(repo.EXISTS_BY_ID);
        assertEquals(expectedCalls, repo.getCalls());

    }

    @Test
    public void testEditPresetBackgroundByIdNull(){
        assertThrows( Exception.class, () -> sut.editPresetBackgroundById(8, null));
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(repo.EXISTS_BY_ID);
        assertEquals(expectedCalls, repo.getCalls());

    }

    @Test
    public void testFontById() throws Exception {
        String font = "font";
        sut.editPresetFontById(0, font);
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(repo.EXISTS_BY_ID);
        expectedCalls.add(repo.UPDATE_FONT_BY_ID);
        assertEquals(expectedCalls, repo.getCalls());
        assertEquals(font, sut.getById(0).font);

    }

    @Test
    public void testEditPresetFontByIdNonValidId() throws Exception {
        String font = "font";
        assertThrows( Exception.class, () -> sut.editPresetFontById(-1, font));
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, repo.getCalls());
        assertEquals(presets.get(0).font, sut.getById(0).font);

    }

    @Test
    public void testEditPresetFontByIdNonExistent(){
        assertThrows( Exception.class, () -> sut.editPresetFontById(8, "font"));
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(repo.EXISTS_BY_ID);
        assertEquals(expectedCalls, repo.getCalls());

    }

    @Test
    public void testEditPresetFontByIdNull(){
        assertThrows( Exception.class, () -> sut.editPresetFontById(8, null));
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(repo.EXISTS_BY_ID);
        assertEquals(expectedCalls, repo.getCalls());

    }

    @Test
    public void testSetAsDefault() throws Exception {
        sut.setAsDefault(0);
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(repo.FIND_BY_ID);
        expectedCalls.add(repo.SAVE);
        assertEquals(expectedCalls, repo.getCalls());

        List<String> expectedCallsBoard = new ArrayList<>();
        expectedCallsBoard.add(boardRepo.FIND_BY_ID);
        expectedCallsBoard.add(boardRepo.SAVE);

        assertEquals(expectedCallsBoard, boardRepo.getCalls());


    }

    @Test
    public void testSetAsDefaultNonExistent() throws Exception {
        assertThrows(Exception.class,()->sut.setAsDefault(10));
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(repo.FIND_BY_ID);
        assertEquals(expectedCalls, repo.getCalls());

        List<String> expectedCallsBoard = new ArrayList<>();
        assertEquals(expectedCallsBoard, boardRepo.getCalls());

    }


}
