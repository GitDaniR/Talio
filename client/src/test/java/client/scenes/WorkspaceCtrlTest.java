package client.scenes;

import client.utils.ServerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WorkspaceCtrlTest {
    @Mock
    private MainCtrl mainCtrl;
    @Mock
    private ServerUtils serverUtils;
    private WorkspaceCtrl workspaceCtrl;

    @BeforeEach
    public void setup(){
        workspaceCtrl = new WorkspaceCtrl(serverUtils,mainCtrl);
    }
    @Test
    public void testInit(){
        assertDoesNotThrow(()-> workspaceCtrl.initialize(null,null));
    }
    @Test
    public void testStop(){
        workspaceCtrl.stop();
        Mockito.verify(serverUtils,times(1)).stop();
    }
    @Test
    public void testSubscribe(){
        workspaceCtrl.subscribeForSocketsWorkspace();
        verify(serverUtils, VerificationModeFactory.times(3))
                .registerForMessages(Mockito.anyString(),Mockito.any(),Mockito.any());
    }
    @Test
    public void testSetUser(){
        workspaceCtrl.setUser("Sandi");
        verify(serverUtils,times(1)).getUserByUsername("Sandi");
        verify(serverUtils,times(1)).addUser(Mockito.any());
    }
    @Test
    public void testCreateBoard(){
        workspaceCtrl.createBoard();
        verify(mainCtrl,times(1)).showNewBoard(Mockito.any());
    }
    @Test
    public void testDisconnect() {
        workspaceCtrl.disconnect();
        verify(mainCtrl,times(1)).showWelcomePage();
    }
}