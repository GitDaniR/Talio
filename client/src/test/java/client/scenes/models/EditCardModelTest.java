package client.scenes.models;

import client.scenes.EditCardCtrl;
import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.Card;
import commons.Subtask;
import jakarta.ws.rs.WebApplicationException;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class EditCardModelTest {

    @Mock
    private MainCtrl mainCtrl;
    @Mock
    private ServerUtils serverUtils;
    @Mock
    private EditCardModel editCardModel;
    @InjectMocks
    private EditCardCtrl editCardCtrl;
    private Card card;


    @BeforeEach
    public void setup(){
        editCardModel = new EditCardModel(serverUtils,mainCtrl);
        card = new Card();
    }


    @Test
    public void constructorTest(){
        assertEquals(editCardModel.getServer(), serverUtils);
        assertEquals(editCardModel.getMainCtrl(), mainCtrl);
    }

    @Test
    public void testNotNull(){
        assertNotNull(mainCtrl);
        assertNotNull(serverUtils);
        assertNotNull(editCardModel);
    }


    @Test
    void testStartWebSockets() {
        editCardModel.startWebSockets(new EditCardCtrl(serverUtils,mainCtrl));
        verify(serverUtils,times(3))
                .registerForMessages(Mockito.anyString(),Mockito.any(),Mockito.any());
    }

    @Test
    void testEditCard() {
        editCardModel.editCard(0,card);
        verify(serverUtils, times(1))
                .editCard(Mockito.anyInt(), Mockito.any(Card.class));

    }

    @Test
    public void testEditCardThrowError(){
        Mockito.when(serverUtils.editCard(Mockito.anyInt(), Mockito.any(Card.class))).thenThrow(WebApplicationException.class);
        assertEquals(editCardModel.editCard(0, card), card);
    }


    @Test
    void testSaveNewSubtask() {
        String subtaskTitle = "SubtaskTitle";
        List<Subtask> expectedSubtasks = new ArrayList<>();
        Subtask subtask = new Subtask(subtaskTitle, false,
                0,card);
        expectedSubtasks.add(subtask);
        editCardModel.saveNewSubtask(subtaskTitle, card);
        verify(serverUtils, times(1)).addSubtask(subtask);

    }
}