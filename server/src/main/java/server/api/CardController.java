package server.api;

import commons.Card;
import commons.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.services.CardService;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {
    private CardService cardService;

    private SimpMessagingTemplate msgs;

    public CardController(CardService cardService,SimpMessagingTemplate msgs){
        this.cardService = cardService;
        this.msgs = msgs;
    }

    //Get mapping to get all cards
    @GetMapping(path = { "", "/" })
    public List<Card> getAllCards(){
        return cardService.getAllCards();
    }

    //Get mapping to get a card by id
    @GetMapping("/{id}")
    public ResponseEntity<Card> getCardById(@PathVariable int id){
        try {
            Card res = cardService.getCardById(id);
            return ResponseEntity.ok(res);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return ResponseEntity.notFound().build();
    }

    //Post mapping to add a card
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Card> addCard(@RequestBody Card card){
        Card saved;
        try {
            saved = cardService.addCard(card);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        if(msgs!=null)
            msgs.convertAndSend("/topic/cards",saved);

        return ResponseEntity.ok(saved);
    }

    //Delete mapping to delete a card
    @DeleteMapping("/{id}")
    public ResponseEntity<Card> removeCard(@PathVariable int id){
        Card deletedRecord;
        try {
            deletedRecord = cardService.removeCardById(id);
        }catch(Exception e){
            return ResponseEntity.notFound().build();
        }

        if(msgs!=null)
            msgs.convertAndSend("/topic/cards",id);

        return ResponseEntity.ok(deletedRecord);
    }

    //Put mapping to update a card
    @PutMapping("/{id}")
    public ResponseEntity<Card> editCard(@PathVariable int id, @RequestBody Card card){
        Card res;
        try {
            res = cardService.editCardById(id, card);
        }catch(Exception e){
            return ResponseEntity.notFound().build();
        }

        if(msgs!=null)
            msgs.convertAndSend("/topic/cards/rename", res);

        return ResponseEntity.ok(res);

    }

    @PutMapping("/{id}/list/{listId}/{index}")
    public ResponseEntity<Card> editCardList(@PathVariable int id, @PathVariable Integer listId,
                                             @PathVariable int index) {
        Card res;
        try {
            res = cardService.editCardByIdList(id, listId, index);
        }catch(Exception e){
            return ResponseEntity.notFound().build();
        }
        if(msgs!=null)
            msgs.convertAndSend("/topic/cards/edit", res);
        return ResponseEntity.ok(res);
    }

    /**
     * Method which removes a tag from a card
     *
     * @param id The id of the card
     * @param tag tag to remove
     * @return response
     */
    @PutMapping("/tags/remove/{id}")
    public ResponseEntity<Card> removeTagFromCard(@PathVariable("id") Integer id,
                                                  @RequestBody Tag tag) {
        Card res = null;
        try {
            res = cardService.removeTag(id, tag);
        }catch(Exception e){
            return ResponseEntity.notFound().build();
        }
        if(msgs!=null)
            msgs.convertAndSend("/topic/cards/remove/tag", res);
        return ResponseEntity.ok(res);
    }

    @PutMapping(path = "{cardId}/preset/{presetId}")
    public ResponseEntity<Card> assignPreset(@PathVariable("cardId") Integer cardId,
                                          @PathVariable("presetId") Integer presetId){
        Card res;
        try{
            res = this.cardService.assignPreset(cardId, presetId);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(res);
    }

}
