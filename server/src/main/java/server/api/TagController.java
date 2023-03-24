package server.api;
import commons.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.TagService;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final TagService tagService;

    /**
     * Constructor for TagController which uses TagService
     * and TagRepository.
     *
     * @param tagService
     */
    public TagController(TagService tagService) {
        this.tagService = tagService;
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
    @PutMapping("/{id}")
    public ResponseEntity<Tag> updateColorById(@PathVariable("id") Integer id,
                                                       @RequestBody String newColor){
        try {
            Tag res = tagService.updateColorById(id, newColor);
            return ResponseEntity.ok(res);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return ResponseEntity.notFound().build();
    }
}
