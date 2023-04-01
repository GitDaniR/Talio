package suts;

import client.scenes.MainCtrl;
import client.scenes.WorkspaceCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.User;
import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class WorkspaceCtrlSut extends WorkspaceCtrl {

    @Inject
    public WorkspaceCtrlSut(ServerUtils server, MainCtrl mainCtrl, User user) {
        super(server, mainCtrl);
        this.setUser(user);
        this.setInputBoardToJoin(new TextField());
        this.setAlreadyJoinedText(new Label());
        this.setBoardsDisplay(new VBox());
        this.setDelay(new PauseTransition());

    }
}
