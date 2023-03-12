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
    }

    public List<BoardList> getBoardLists() {
        if(!lists.isEmpty()) return lists;

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
        return cards;



    }
    public void addBoardList(BoardList list) {
        lists.add(list);

    }

    public void addCard(Card card, BoardList list){

        for(BoardList l: lists){
            if(Objects.equals(list, l)){
                l.cards.add(card);
                return;
            }
        }



    }



}
