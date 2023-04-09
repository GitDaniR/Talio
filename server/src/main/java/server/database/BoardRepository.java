package server.database;

import commons.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    @Modifying
    @Query("UPDATE Board b SET b.title = :title WHERE b.id = :id")
    public void updateBoardById(@Param("id") Integer id, @Param("title") String title);
}
