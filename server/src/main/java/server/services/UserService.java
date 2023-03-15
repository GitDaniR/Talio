package server.services;

import commons.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import server.database.UserRepository;

@Service
public class UserService {

    /**
     * Method which returns a user by id from repo.
     * @param id
     * @param repo
     * @return user
     * @throws Exception if id is not in repo.
     */
    public ResponseEntity<User> getById(Integer id, UserRepository repo) throws Exception {
        if (id < 0 || !repo.existsById(id)) {
            throw new Exception("Invalid id");
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * Method which adds a new user to repo.
     * @param user
     * @param repo
     * @return the saved board
     * @throws Exception if title is null.
     */
    public ResponseEntity<User> add(User user, UserRepository repo) throws Exception {
        if (user.username == null) {
            throw new Exception("Invalid title");
        }
        return ResponseEntity.ok(repo.save(user));
    }

    /**
     * Method which deletes a user by id from repo.
     * @param id
     * @param repo
     * @return the deleted user
     * @throws Exception if id is not in repo.
     */
    public ResponseEntity<User> deleteById(Integer id, UserRepository repo) throws Exception {
        if (id < 0 || !repo.existsById(id)) {
            throw new Exception("Invalid id");
        }
        ResponseEntity<User> deletedRecord = ResponseEntity.ok(repo.findById(id).get());
        repo.deleteById(id);
        return deletedRecord;
    }
}
