package server.services;

import commons.Card;
import commons.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import server.database.TagRepository;

@Service
public class TagService {

    private final TagRepository repo;

    public TagService(TagRepository repo) {
        this.repo = repo;
    }


    /**
     * Method which returns a tag by id from repo.
     * @param id
     * @return tag
     * @throws Exception if id is not in repo.
     */
    public ResponseEntity<Tag> getById(Integer id) throws Exception {
        if (id < 0 || !repo.existsById(id)) {
            throw new Exception("Invalid id");
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }


    /**
     * Method which adds a new tag to repo.
     * @param tag
     * @return the saved board
     * @throws Exception if title is null.
     */
    public ResponseEntity<Tag> add(Tag tag) throws Exception {
        if (tag.title == null) {
            throw new Exception("Invalid title");
        }
        return ResponseEntity.ok(repo.save(tag));
    }


    /**
     * Method which deletes a tag by id from repo.
     * @param id
     * @return the deleted tag
     * @throws Exception if id is not in repo.
     */
    public ResponseEntity<Tag> deleteById(Integer id) throws Exception {
        if (id < 0 || !repo.existsById(id)) {
            throw new Exception("Invalid id");
        }
        ResponseEntity<Tag> deletedRecord = ResponseEntity.ok(repo.findById(id).get());
        repo.deleteById(id);
        return deletedRecord;
    }

    public TagRepository getRepo(){
        return this.repo;
    }


    /**
     * Method which changes the color of a tag
     * @param id the id of the tag
     * @param newColor the new color of the tag
     * @return the saved tag
     * @throws Exception if id is not in repo.
     */
    public Tag updateColorById(Integer id, String newColor) throws Exception {
        Tag res = repo.findById(id).orElseThrow(
                ()->new Exception("Tag with id: " + id +" not found")
        );
        res.color = newColor;
        return repo.save(res);
    }


    /**
     * Method which changes the title of a tag
     * @param id the id of the tag
     * @param newTitle the new title of the tag
     * @return the saved tag
     * @throws Exception if id is not in repo.
     */
    public Tag updateTitleById(Integer id, String newTitle) throws Exception {
        Tag res = repo.findById(id).orElseThrow(
                ()->new Exception("Tag with id: " + id +" not found")
        );
        res.title = newTitle;
        return repo.save(res);
    }

    /**
     * Method which adds a card to a tag
     * @param id the id of the tag
     * @param card the card
     * @return the saved tag
     * @throws Exception if id is not in repo.
     */
    public Tag addCard(Integer id, Card card) throws Exception {
        Tag res = repo.findById(id).orElseThrow(
                ()->new Exception("Tag with id: " + id +" not found")
        );
        if(!res.cards.contains(card)) res.cards.add(card);
        return repo.save(res);
    }

    /**
     * Method which removes a card from a tag
     * @param id the id of the tag
     * @param card the card
     * @return the saved tag
     * @throws Exception if id is not in repo.
     */
    public Tag removeCard(Integer id, Card card) throws Exception {
        Tag res = repo.findById(id).orElseThrow(
                ()->new Exception("Tag with id: " + id +" not found")
        );
        res.cards.remove(card);
        return repo.save(res);
    }
}
