package server.api;
import commons.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.UserRepository;
import server.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserRepository repo;



    /**
     * Constructor for UserController which uses UserService and UserRepository.
     *
     * @param userService
     * @param repo
     */
    public UserController(UserService userService, UserRepository repo) {
        this.userService = userService;
        this.repo = repo;
    }

    /**
     * Method which returns all users in repo.
     * @return all users
     */
    @GetMapping(path = { "", "/" })
    public List<User> getAll(){
        return repo.findAll();
    }

    /**
     * Method which returns a user by an id from repo.
     * @param id
     * @return a user
     */

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable("id") Integer id) {
        ResponseEntity<User> found;
        try {
            found = this.userService.getById(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return found;
    }

    /**
     * Method which retrieves user by its username
     * @param username
     * @return
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getByUsername(@PathVariable("username") String username){
        try {
            return this.userService.getByUsername(username);
        } catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Method which adds a new user to repo.
     * @param user
     * @return the saved user or BAD_REQUEST
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<User> add(@RequestBody User user) {
        ResponseEntity<User> saved;
        try {
            saved = this.userService.add(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return saved;
    }

    /**
     * Method which deletes a user by id from repo.
     * @param id
     * @return the deleted user or BAD_REQUEST
     */
    @DeleteMapping(path = { "/{id}" })
    public ResponseEntity<User> deleteById(@PathVariable("id") Integer id) {
        ResponseEntity<User> deletedRecord;
        try {
            deletedRecord = this.userService.deleteById(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return deletedRecord;
    }

    /**
     * Method that adds board to list of joined boards by the user
     * @param userId
     * @param boardId
     * @return
     */
    @PutMapping(path = "{userId}/boards/{boardId}")
    public ResponseEntity<User> joinBoard(@PathVariable("userId") Integer userId,
                                          @PathVariable("boardId") Integer boardId){
        try{
            return this.userService.joinBoard(userId, boardId);

        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(path = "{userId}/boards/{boardId}")
    public ResponseEntity<User> removeBoard(@PathVariable("userId") Integer userId,
                                            @PathVariable("boardId") Integer boardId){
        try{
            return this.userService.removeBoard(userId, boardId);

        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }




}
