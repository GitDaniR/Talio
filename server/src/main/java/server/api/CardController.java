package server.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.services.CardService;

@RestController
@RequestMapping("/api/cards")
public class CardController {
    private CardService cardService;

    public CardController(CardService cardService){
        this.cardService = cardService;
    }
}
