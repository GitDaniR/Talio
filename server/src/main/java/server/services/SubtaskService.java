package server.services;

import commons.Subtask;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import server.database.SubtaskRepository;

@Service
public class SubtaskService {

    private final SubtaskRepository repo;

    public SubtaskService(SubtaskRepository repo) {
        this.repo = repo;
    }


    /**
     * Method which returns a subtask by id from repo.
     * @param id
     * @return user
     * @throws Exception if id is not in repo.
     */
    public ResponseEntity<Subtask> getById(Integer id) throws Exception {
        if (id < 0 || !repo.existsById(id)) {
            throw new Exception("Invalid id");
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }


    /**
     * Method which adds a new subtask to repo.
     * @param subtask
     * @return the saved board
     * @throws Exception if title is null.
     */
    public ResponseEntity<Subtask> add(Subtask subtask) throws Exception {
        if (subtask.title == null) {
            throw new Exception("Invalid title");
        }
        return ResponseEntity.ok(repo.save(subtask));
    }


    /**
     * Method which deletes a subtask by id from repo.
     * @param id
     * @return the deleted subtask
     * @throws Exception if id is not in repo.
     */
    public ResponseEntity<Subtask> deleteById(Integer id) throws Exception {
        if (id < 0 || !repo.existsById(id)) {
            throw new Exception("Invalid id");
        }
        ResponseEntity<Subtask> deletedRecord = ResponseEntity.ok(repo.findById(id).get());
        repo.deleteById(id);
        return deletedRecord;
    }

    public SubtaskRepository getRepo(){
        return this.repo;
    }

}
