package server.api;

import commons.Card;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.CardService;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {
    private CardService cardService;

    public CardController(CardService cardService){
        this.cardService = cardService;
    }

    //Get mapping to get all cards. Currently only intended for testing purposes
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
        try {
            return ResponseEntity.ok(cardService.addCard(card));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.badRequest().build();
    }

    //Delete mapping to delete a card
    @DeleteMapping("/{id}")
    public ResponseEntity<Card> removeCard(@PathVariable int id){
        try {
            Card res = cardService.removeCardById(id);
            return ResponseEntity.ok(res);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return ResponseEntity.notFound().build();
    }

    //Put mapping to update a card
    @PutMapping("/{id}")
    public ResponseEntity<Card> editCard(@PathVariable int id, @RequestBody Card card){
        try {
            Card res = cardService.editCardById(id, card);
            return ResponseEntity.ok(res);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return ResponseEntity.notFound().build();
    }
}
