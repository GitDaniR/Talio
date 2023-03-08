package server.services;

import org.springframework.stereotype.Service;
import server.database.CardRepository;

@Service
public class CardService {
    private CardRepository cardRepo;

    public CardService(CardRepository cardRepo){
        this.cardRepo = cardRepo;
    }
}
