package server.services;

import commons.BoardList;
import commons.Card;
import commons.Tag;
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

    //Method to edit a card by id
    @Transactional
    public Card editCardById(int id, Card card)throws Exception{
        Card res = cardRepo.findById(id).orElseThrow(
            ()->new Exception("Card with id: " + id +" not found")
        );
        res.title = card.title;
        res.description = card.description;
        res.tags = card.tags;

        Card saved = cardRepo.save(res);
        return saved;
    }

    @Transactional
    public Card editCardByIdList(int id, int listId, int index)throws Exception{
        Card res = cardRepo.findById(id).orElseThrow(
                ()->new Exception("Card with id: " + id +" not found")
        );
        BoardList list = listRepo.findById(listId).orElseThrow(
                ()->new Exception("List with id: " + id +" not found"));

        if(res.listId == listId){
            if(index>res.index){
                cardRepo.shiftCardsBetweenUp(res.index, index, listId);
            }else{
                cardRepo.shiftCardsBetweenDown(index, res.index, listId);
            }
        }else{
            cardRepo.shiftCardsLeft(res.index, res.listId);
            cardRepo.shiftCardsRight(index, listId);
        }
        res.index = index;
        res.listId = listId;
        res.list = list;
        return cardRepo.save(res);
    }

    /**
     * Method which removes a tag from a card
     * @param id the id of the card
     * @param tag the tag
     * @return the saved card
     * @throws Exception if id is not in repo.
     */
    public Card removeTag(Integer id, Tag tag) throws Exception {
        Card res = cardRepo.findById(id).orElseThrow(
                ()->new Exception("Card with id: " + id +" not found")
        );

        // check if the card has the tag and remove it
        Tag toRemove = null;
        for(Tag t: res.tags){
            // I cannot expect that tag parameter is the same as the one
            // in the card so I have to compare ids
            if(t.id == tag.id) toRemove = t;
        }
        if(toRemove != null) res.tags.remove(toRemove);

        return cardRepo.save(res);
    }
}
