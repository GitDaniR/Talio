package server.services;

import commons.Board;
import commons.BoardList;
import commons.Card;
import org.springframework.stereotype.Service;
import server.database.BoardListRepository;
import server.database.CardRepository;


import javax.transaction.Transactional;
import java.util.List;

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
        Card res = cardRepo.findById(id).orElseThrow(
            ()->new Exception("Card with id: " + id +" not found")
        );
        return res;
    }

    @Transactional
    //Method to add card to repo
    public Card addCard(Card card) throws Exception{
        if(!listRepo.existsById(card.listId))
            throw new Exception("List does not exist");
        if(card.id != null && cardRepo.existsById(card.id))
            throw new Exception("Card with id: " + card.id +" already exists");
        cardRepo.shiftCardsRight(card.index, card.listId);
        return cardRepo.save(card);
    }

    @Transactional
    //Method to remove card from repo
    public Card removeCardById(int id) throws Exception{
        Card res = cardRepo.findById(id).orElseThrow(
            ()->new Exception("Card with id: " + id +" not found")
        );
        cardRepo.shiftCardsLeft(res.index, res.listId);
        cardRepo.deleteById(id);
        return res;
    }

    //Method to edit a card by listId
    public Card editCardById(int id, Card card)throws Exception{
        Card res = cardRepo.findById(id).orElseThrow(
            ()->new Exception("Card with id: " + id +" not found")
        );
        res.title = card.title;
        res.description = card.description;
        return cardRepo.save(res);
    }

    public Card editCardByIdList(int id, int listId)throws Exception{
        Card res = cardRepo.findById(id).orElseThrow(
                ()->new Exception("Card with id: " + id +" not found")
        );
        BoardList list = listRepo.findById(listId).orElseThrow(()->new Exception("List with id: " + id +" not found"));
        res.listId = listId;
        res.list = list;
        return cardRepo.save(res);
    }
}
