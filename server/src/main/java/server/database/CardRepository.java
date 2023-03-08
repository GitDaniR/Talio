package server.database;

import commons.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CardRepository extends JpaRepository<Card, Integer> {
    @Modifying
    @Query("UPDATE Card c SET c.index = c.index + 1 WHERE c.index >= :index AND c.listId = :listId")
    void shiftCardsRight(int index, int listId);

    @Modifying
    @Query("UPDATE Card c SET c.index = c.index - 1 WHERE c.index > :index AND c.listId = :listId")
    void shiftCardsLeft(int index, int listId);
}
