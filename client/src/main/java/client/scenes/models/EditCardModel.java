package client.scenes.models;
import client.scenes.EditCardCtrl;
import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.Card;
import commons.Subtask;
import jakarta.ws.rs.WebApplicationException;
import javafx.application.Platform;

public class EditCardModel {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;


    public EditCardModel(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public MainCtrl getMainCtrl() {
        return mainCtrl;
    }

    public ServerUtils getServer(){return  server;}


    public void startWebSockets(EditCardCtrl editCardCtrl){
        server.registerForMessages("/topic/subtasks", Integer.class, cardId -> {
            Platform.runLater(() -> editCardCtrl.setCardToEdit(server.getCardById(cardId)));});
        server.registerForMessages("/topic/cards/rename", Card.class, card -> {
            Platform.runLater(() -> editCardCtrl.updateCard(card));});
        server.registerForMessages("/topic/tags", Integer.class, boardId -> {
            Platform.runLater(() -> editCardCtrl.overwriteTags(server.getBoardByID(boardId).tags));});
    }

    public Card editCard(int cardToUpdateId, Card newCard){
        try {
            return server.editCard(cardToUpdateId, newCard);
        } catch (WebApplicationException e) {
            System.out.println(e.getStackTrace());
            return newCard;
        }

    }

    public void saveNewSubtask(String subtaskTitle, Card card){
        Subtask subtask = new Subtask(subtaskTitle, false,
                card.subtasks.size(),card);
        card.subtasks.add(subtask);
        server.addSubtask(subtask);
    }

}
