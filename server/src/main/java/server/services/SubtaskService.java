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

    /**
     * Method that updates the "done" boolean field of a subtask
     *
     * @param id Id of the subtask
     * @param done new boolean value
     * @return changed subtask
     * @throws Exception if id not found in repository
     */
    public Subtask updateSubtaskStatus(Integer id, String done) throws Exception {
        Subtask res = repo.findById(id).orElseThrow(
                ()->new Exception("Subtask with id: " + id +" not found")
        );
        res.done = Boolean.valueOf(done);
        return repo.save(res);
    }


    /**
     * Method that updates the index of a subtask
     *
     * @param id Id of the subtask
     * @param index new index of the subtask
     * @return changed subtask
     * @throws Exception if id not found in repository
     */
    public Subtask updateIndexById(Integer id, String index) throws Exception {
        Subtask res = repo.findById(id).orElseThrow(
                ()->new Exception("Subtask with id: " + id +" not found")
        );
        res.index = Integer.valueOf(index);
        return repo.save(res);
    }

    /**
     * Method that updates the title of a subtask
     *
     * @param id Id of the subtask
     * @param title new title of the subtask
     * @return changed subtask
     * @throws Exception if id not found in repository
     */
    public Subtask updateTitleById(Integer id, String title) throws Exception {
        Subtask res = repo.findById(id).orElseThrow(
                ()->new Exception("Subtask with id: " + id +" not found")
        );
        res.title = title;
        return repo.save(res);
    }
}
