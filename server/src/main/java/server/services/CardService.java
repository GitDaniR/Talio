package server.services;

import commons.Card;
import org.springframework.stereotype.Service;
import server.database.BoardListRepository;
import server.database.CardRepository;

import javax.swing.tree.ExpandVetoException;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.Optional;

@Service
public class CardService {
    private CardRepository cardRepo;
    private BoardListRepository listRepo;

    //Constructor fit for dependency injection
    public CardService(CardRepository cardRepo, BoardListRepository listRepo){
        this.cardRepo = cardRepo;
        this.listRepo = listRepo;
    }

    //Method to get all saved cards (mainly intended for testing purposes)
    public List<Card> getAllCards(){
        return cardRepo.findAll();
    }

    //Method to get card by id from repo
    //Throws exception if card is not found
    public Card getCardById(int id) throws Exception{
        Card res = cardRepo.findById(id).orElseThrow(()->new Exception("Card with id: " + id +" not found"));
        return res;
    }

    //Method to add card to repo
    public Card addCard(Card card) throws Exception{
        if(!listRepo.existsById(card.listId))
            throw new Exception("List does not exist");
        if(card.id != null && cardRepo.existsById(card.id))
            throw new Exception("Card with id: " + card.id +" already exists");
        return cardRepo.save(card);
    }

    //Method to remove card from repo
    public Card removeCardById(int id) throws Exception{
        Card res = cardRepo.findById(id).orElseThrow(()->new Exception("Card with id: " + id +" not found"));
        cardRepo.deleteById(id);
        return res;
    }

    //Method to edit a card
    public Card editCardById(int id, Card card)throws Exception{
        card.id = id;
        if(!listRepo.existsById(card.listId))
            throw new Exception("List does not exist");
        if(!cardRepo.existsById(id))
            throw new Exception("Card with id: " + card.id +" does not exist");
        return cardRepo.save(card);
    }
}
