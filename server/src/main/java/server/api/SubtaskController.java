package server.api;
import commons.Subtask;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.SubtaskService;

import java.util.List;

@RestController
@RequestMapping("/api/subtasks")
public class SubtaskController {
    private final SubtaskService subtaskService;

    /**
     * Constructor for SubtaskController which uses SubtaskService
     * and SubtaskRepository.
     *
     * @param subtaskService
     */
    public SubtaskController(SubtaskService subtaskService) {
        this.subtaskService = subtaskService;
    }

    /**
     * Method which returns all subtasks in repo.
     * @return all subtasks
     */
    @GetMapping(path = { "", "/" })
    public List<Subtask> getAll(){
        return subtaskService.getRepo().findAll();
    }

    /**
     * Method which returns a subtask by an id from repo.
     * @param id
     * @return a subtask
     */

    @GetMapping("/{id}")
    public ResponseEntity<Subtask> getById(@PathVariable("id") Integer id) {
        ResponseEntity<Subtask> found;
        try {
            found = this.subtaskService.getById(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return found;
    }


    /**
     * Method which adds a new subtask to repo.
     * @param subtask
     * @return the saved subtask or BAD_REQUEST
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Subtask> add(@RequestBody Subtask subtask) {
        ResponseEntity<Subtask> saved;
        try {
            saved = this.subtaskService.add(subtask);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return saved;
    }

    /**
     * Method which deletes a subtask by id from repo.
     * @param id
     * @return the deleted subtask or BAD_REQUEST
     */
    @DeleteMapping(path = { "/{id}" })
    public ResponseEntity<Subtask> deleteById(@PathVariable("id") Integer id) {
        ResponseEntity<Subtask> deletedRecord;
        try {
            deletedRecord = this.subtaskService.deleteById(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return deletedRecord;
    }

    /**
     * Method that updates the "done" boolean field of a subtask
     *
     * @param id Id of the subtask
     * @param done New boolean value
     * @return response
     */
    @PutMapping("/status/{id}")
    public ResponseEntity<Subtask> updateSubtaskStatus(@PathVariable("id") Integer id,
                                                       @RequestBody String done){
        try {
            Subtask res = subtaskService.updateSubtaskStatus(id, done);
            return ResponseEntity.ok(res);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return ResponseEntity.notFound().build();
    }


    /**
     * Method that updates the index of a subtask
     *
     * @param id Id of the subtask
     * @param index New index of the card
     * @return response
     */
    @PutMapping("/index/{id}")
    public ResponseEntity<Subtask> updateIndexById(@PathVariable("id") Integer id,
                                                       @RequestBody String index){
        try {
            Subtask res = subtaskService.updateIndexById(id, index);
            return ResponseEntity.ok(res);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return ResponseEntity.notFound().build();
    }
}
