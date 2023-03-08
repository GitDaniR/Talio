package server.services;

import commons.Card;
import org.springframework.stereotype.Service;
import server.database.CardRepository;

import java.util.List;

@Service
public class CardService {
    private CardRepository cardRepo;

    //Constructor fit for dependency injection
    public CardService(CardRepository cardRepo){
        this.cardRepo = cardRepo;
    }

    //Method to get all saved cards (mainly intended for testing purposes)
    public List<Card> getAllCards(){
        return cardRepo.findAll();
    }
}
