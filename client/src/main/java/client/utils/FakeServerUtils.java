package client.utils;

import commons.BoardList;
import commons.Card;

import java.util.ArrayList;
import java.util.List;
public class FakeServerUtils {

    public List<BoardList> getBoardLists() {
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

        List<Card> cards = new ArrayList<>();

        Card card1 = new Card("Card1", "Description", 0 , null);
        Card card2 = new Card("Card2", "Description", 0, null);
        Card card3 = new Card("Card3", "Description", 0 , null);

        cards.add(card1);
        cards.add(card2);
        cards.add(card3);

        return cards;


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
        System.out.println("Successfully added the list");
        return;

    }

    public void addCard(Card card){
        System.out.println("Successfully added the card");
        return;
    }
}
