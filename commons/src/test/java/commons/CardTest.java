package commons;
//import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;




public class CardTest {

    public static BoardList list;

//    @BeforeEach
//    public void setup(){
//        BoardList list = new BoardList("List", null);
//    }

    @Test
    public void checkConstructor(){
        var card = new Card("card", "description", 0 ,list ,null);
        assertEquals(card.title, "card");
        assertEquals(card.description, "description");
        assertEquals(card.list, list);


    }
//    @Test
//    public void checkAddTag(){
//        var tag = new Tag("tag", "colour");
//        var listTags = new ArrayList<Tag>();
//        listTags.add(tag);
//        var card = new Card("card", "description", 0 ,list ,null);
//        card.addTag(tag);
//
//        assertEquals(card.tags, listTags);
//
//    }

    @Test
    public void equalsHashCode(){
        var card1 = new Card("card", "description", 0 ,list , null);
        var card2 = new Card("card", "description", 0 ,list , null);

        assertEquals(card1, card2);
        assertEquals(card1.hashCode(),card2.hashCode());
    }

    @Test
    public void notEqualsHashCode(){
        var card1 = new Card("card1", "description", 0 ,list , null);
        var card2 = new Card("card2", "description", 0 ,list , null);

        assertNotEquals(card1, card2);
        assertNotEquals(card1.hashCode(), card2.hashCode());

    }

    @Test
    public void hasToString(){
        var card1 = new Card("card", "description", 0 ,list, null );
        assertTrue(card1.toString().contains("card"));
        assertTrue(card1.toString().contains("description"));
        assertTrue(card1.toString().contains("0"));
    }


}
