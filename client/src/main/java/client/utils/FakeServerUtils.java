package client.utils;

import commons.BoardList;
import commons.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FakeServerUtils {

    // I have changed the fakeServer setup a bit to make it easier to see if adding cards works
    private List<BoardList> lists = new ArrayList<>();

    public void setup(){
        lists = getBoardLists();

        Card card1 = new Card("Card1", "Description", 0 , null);
        Card card2 = new Card("Card2", "Description", 0, null);
        Card card3 = new Card("Card3", "Description", 0 , null);

        lists.get(0).addCard(card1);
        lists.get(0).addCard(card2);
        lists.get(1).addCard(card3);



    }

    public List<BoardList> getBoardLists() {
        if(!lists.isEmpty()) return lists;

        List<BoardList> lists = new ArrayList<>();

        BoardList list1 = new BoardList("TODO");
        BoardList list2 = new BoardList("TASKS");
        BoardList list3  = new BoardList("THINGS");

        lists.add(list1);
        lists.add(list2);
        lists.add(list3);

        return lists;



    }

    public List<Card> getCards(BoardList list){

        for(BoardList l: lists){
            if(Objects.equals(list, l)){
                return l.cards;
            }
        }

        return getCards(0);



    }
    public List<Card> getCards(int id){

        List<Card> cards = new ArrayList<>();

        Card card1 = new Card("Card1", "Description", 0 , null);
        Card card2 = new Card("Card2", "Description", 0, null);
        Card card3 = new Card("Card3", "Description", 0 , null);

        cards.add(card1);
        cards.add(card2);
        cards.add(card3);

        return cards;



    }
    public void addBoardList(BoardList list) {
        lists.add(list);
        return;



    }

    public void addCard(Card card, BoardList list){

        for(BoardList l: lists){
            if(Objects.equals(list, l)){
                l.cards.add(card);
                return;
            }
        }
        return;



    }



}
