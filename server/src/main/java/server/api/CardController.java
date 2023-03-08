package server.api;

import commons.Card;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

}
