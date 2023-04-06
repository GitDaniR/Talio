package server.api;

import commons.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.services.TagService;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final TagService tagService;

    private SimpMessagingTemplate msgs;

    /**
     * Constructor for TagController which uses TagService
     * and TagRepository.
     *
     * @param tagService
     */
    public TagController(TagService tagService, SimpMessagingTemplate msgs) {
        this.tagService = tagService;
        this.msgs = msgs;
    }

    /**
     * Method which returns all tags in repo.
     * @return all tags
     */
    @GetMapping(path = { "", "/" })
    public List<Tag> getAll(){
        return tagService.getRepo().findAll();
    }

    /**
     * Method which returns a tag by an id from repo.
     * @param id
     * @return a tag
     */

    @GetMapping("/{id}")
    public ResponseEntity<Tag> getById(@PathVariable("id") Integer id) {
        ResponseEntity<Tag> found;
        try {
            found = this.tagService.getById(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return found;
    }

    @GetMapping("/board/{id}")
    public List<Tag> getByBoardId(@PathVariable("id") Integer id) {
        List<Tag> found = getAll();
        found.removeIf(t -> t.boardId != id);
        return found;
    }


    /**
     * Method which adds a new tag to repo.
     * @param tag
     * @return the saved tag or BAD_REQUEST
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Tag> add(@RequestBody Tag tag) {
        ResponseEntity<Tag> saved;
        try {
            saved = this.tagService.add(tag);
            if(msgs!=null)
                msgs.convertAndSend("/topic/tags",tag.boardId);
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
    public ResponseEntity<Tag> deleteById(@PathVariable("id") Integer id) {
        ResponseEntity<Tag> deletedRecord;
        try {
            deletedRecord = this.tagService.deleteById(id);
            if(msgs!=null)
                msgs.convertAndSend("/topic/tags",
                        Objects.requireNonNull(deletedRecord.getBody()).boardId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return deletedRecord;
    }


    /**
     * Method which updates a tag's color.
     *
     * @param id The id of the tag
     * @param newColor The new color of the tag
     * @return response
     */
    @PutMapping("/color/{id}")
    public ResponseEntity<Tag> updateColorById(@PathVariable("id") Integer id,
                                                       @RequestBody String newColor){
        try {
            Tag res = tagService.updateColorById(id, newColor);
            if(msgs!=null)
                msgs.convertAndSend("/topic/tags",res.boardId);
            return ResponseEntity.ok(res);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Method which updates a tag's font's color.
     *
     * @param id The id of the tag
     * @param newColor The new color of the tag's font
     * @return response
     */
    @PutMapping("/font/{id}")
    public ResponseEntity<Tag> updateFontById(@PathVariable("id") Integer id,
                                               @RequestBody String newColor){
        try {
            Tag res = tagService.updateFontById(id, newColor);
            if(msgs!=null)
                msgs.convertAndSend("/topic/tags",res.boardId);
            return ResponseEntity.ok(res);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return ResponseEntity.notFound().build();
    }


    /**
     * Method which updates the title of a tag
     *
     * @param id The id of the tag
     * @param newTitle The new title
     * @return response
     */
    @PutMapping("/title/{id}")
    public ResponseEntity<Tag> updateTitleById(@PathVariable("id") Integer id,
                                               @RequestBody String newTitle){
        try {
            Tag res = tagService.updateTitleById(id, newTitle);
            if(msgs!=null)
                msgs.convertAndSend("/topic/tags",res.boardId);
            return ResponseEntity.ok(res);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return ResponseEntity.notFound().build();
    }
}
