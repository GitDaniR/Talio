package server.api;
import commons.Subtask;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.services.SubtaskService;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/api/subtasks")
public class SubtaskController {
    private final SubtaskService subtaskService;

    private SimpMessagingTemplate msgs;

    /**
     * Constructor for SubtaskController which uses SubtaskService
     * and SubtaskRepository.
     *
     * @param subtaskService
     */
    public SubtaskController(SubtaskService subtaskService,SimpMessagingTemplate msgs) {
        this.subtaskService = subtaskService;
        this.msgs=msgs;
    }

    /**
     * Method which returns all subtasks in repo.
     * @return all subtasks
     */
    @GetMapping(path = { "", "/" })
    @ResponseBody
    public List<Subtask> getAll(){
        return subtaskService.getRepo().findAll();
    }

    /**
     * Method which returns a subtask by an id from repo.
     * @param id
     * @return a subtask
     */

    @GetMapping("/{id}")
    @ResponseBody
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
    @ResponseBody
    public ResponseEntity<Subtask> add(@RequestBody Subtask subtask) {
        ResponseEntity<Subtask> saved;
        try {
            saved = this.subtaskService.add(subtask);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        if(msgs!=null)
            msgs.convertAndSend("/topic/subtasks",subtask.cardId);

        return saved;
    }

    /**
     * Method which deletes a subtask by id from repo.
     * @param id
     * @return the deleted subtask or BAD_REQUEST
     */
    @DeleteMapping(path = { "/{id}" })
    @ResponseBody
    public ResponseEntity<Subtask> deleteById(@PathVariable("id") Integer id) {
        ResponseEntity<Subtask> deletedRecord;
        try {
            deletedRecord = this.subtaskService.deleteById(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        if(msgs!=null)
            msgs.convertAndSend("/topic/subtasks",
                    Objects.requireNonNull(deletedRecord.getBody()).cardId);

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
    @ResponseBody
    public ResponseEntity<Subtask> updateSubtaskStatus(@PathVariable("id") Integer id,
                                                       @RequestBody String done){
        try {
            Subtask res = subtaskService.updateSubtaskStatus(id, done);

            if(msgs!=null)
                msgs.convertAndSend("/topic/subtasks", res.cardId);

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
    @ResponseBody
    public ResponseEntity<Subtask> updateIndexById(@PathVariable("id") Integer id,
                                                       @RequestBody String index){
        try {
            Subtask res = subtaskService.updateIndexById(id, index);

            if(msgs!=null)
                msgs.convertAndSend("/topic/subtasks", res.cardId);

            return ResponseEntity.ok(res);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Method that updates the title of a subtask
     *
     * @param id Id of the subtask
     * @param title New title of the card
     * @return response
     */
    @PutMapping("/title/{id}")
    @ResponseBody
    public ResponseEntity<Subtask> updateTitleById(@PathVariable("id") Integer id,
                                                   @RequestBody String title){
        try {
            Subtask res = subtaskService.updateTitleById(id, title);

            if(msgs!=null)
                msgs.convertAndSend("/topic/subtasks", res.cardId);

            return ResponseEntity.ok(res);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return ResponseEntity.notFound().build();
    }
}
