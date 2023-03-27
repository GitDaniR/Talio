package server.database;

import commons.Subtask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SubtaskRepository extends JpaRepository<Subtask, Integer> {
    @Modifying
    @Query("UPDATE Subtask t SET t.index = t.index - 1 WHERE t.index > :index AND t.cardId = :cardId")
    void shiftSubtasksDown(int index, int cardId);
}
