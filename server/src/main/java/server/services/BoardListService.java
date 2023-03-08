package server.services;

import commons.BoardList;
import org.springframework.stereotype.Service;
import server.database.BoardListRepository;

import java.util.List;

@Service
public class BoardListService {
    private final BoardListRepository repo;

    public BoardListService(BoardListRepository repo) {
        this.repo = repo;
    }

    public List<BoardList> findAll() {
        return this.repo.findAll();
    }
}
