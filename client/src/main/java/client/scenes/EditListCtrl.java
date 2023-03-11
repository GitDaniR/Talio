package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.BoardList;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

public class EditListCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private BoardList boardListToEdit;

    @FXML
    private TextField title;

    @Inject
    public EditListCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void setBoardListToEdit(BoardList boardListToEdit){
        this.boardListToEdit=boardListToEdit;
    }

    public void editList() {
        try {
            server.updateBoardListTitle(boardListToEdit.id,title.getText());
        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        clearFields();
        mainCtrl.showBoard();
    }
    private void clearFields() {
        title.clear();
    }

    public void cancel() {
        clearFields();
        mainCtrl.showBoard();
    }
}
