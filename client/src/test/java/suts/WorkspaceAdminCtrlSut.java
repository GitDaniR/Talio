package suts;

import client.scenes.MainCtrl;
import client.scenes.WorkspaceAdminCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.User;
import javafx.scene.layout.VBox;

public class WorkspaceAdminCtrlSut extends WorkspaceAdminCtrl {

    @Inject
    public WorkspaceAdminCtrlSut(ServerUtils server, MainCtrl mainCtrl, User user) {
        super(server, mainCtrl);
        this.setUser(user);
        this.setBoardsDisplay(new VBox());

    }
}
