package client.scenes;

import client.utils.ServerUtils;
import commons.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class EditCardTest {

    @Mock
    private MainCtrl mainCtrl;
    @Mock
    private ServerUtils serverUtils;

    private EditCardCtrl editCardCtrl;
    private Card card;


    @BeforeEach
    public void setup(){
        editCardCtrl = new EditCardCtrl(serverUtils,mainCtrl);
        card = new Card();
    }

    @Test
    public void testNotNull(){
        assertNotNull(mainCtrl);
        assertNotNull(serverUtils);
        assertNotNull(editCardCtrl);
    }


    @Test
    void testStartWebSockets() {
        editCardCtrl.subscribeToSocketsEditCardCtrl();
        verify(serverUtils,times(4))
                .registerForMessages(Mockito.anyString(),Mockito.any(),Mockito.any());
    }
}