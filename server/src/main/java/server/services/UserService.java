package server.services;


import commons.Board;
import commons.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;
import server.database.UserRepository;

@Service
public class UserService {


    private final UserRepository repo;


    private final BoardRepository boardRepository;


    public UserService(UserRepository repo,
                       BoardRepository boardRepository) {
        this.repo = repo;
        this.boardRepository = boardRepository;
    }


    /**
     * Method which returns a user by id from repo.
     * @param id=
     * @return user
     * @throws Exception if id is not in repo.
     */
    public ResponseEntity<User> getById(Integer id) throws Exception {
        if (id < 0 || !repo.existsById(id)) {
            throw new Exception("Invalid id");
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * Method which adds a new user to repo.
     * @param user
     * @return the saved board
     * @throws Exception if title is null.
     */
    public ResponseEntity<User> add(User user) throws Exception {
        if (user.username == null) {
            throw new Exception("Invalid title");
        }
        return ResponseEntity.ok(repo.save(user));
    }

    /**
     * Method which deletes a user by id from repo.
     * @param id
     * @return the deleted user
     * @throws Exception if id is not in repo.
     */
    public ResponseEntity<User> deleteById(Integer id) throws Exception {
        if (id < 0 || !repo.existsById(id)) {
            throw new Exception("Invalid id");
        }
        ResponseEntity<User> deletedRecord = ResponseEntity.ok(repo.findById(id).get());
        repo.deleteById(id);
        return deletedRecord;
    }

    /**
     * Method which is retrieving the user based on its username
     * @param username
     * @return User object
     * @throws Exception
     */
    public ResponseEntity<User> getByUsername(String username)throws Exception{
        if(username.equals("")){
            throw new Exception("Invalid id");
        }
        return ResponseEntity.ok(repo.findByUsername(username));
    }

    /**
     * Method that adds board with the BoardID to the list of
     * joined boards by the user
     * @param userId
     * @param boardId
     * @return
     * @throws Exception
     */
    public ResponseEntity<User> joinBoard(Integer userId, Integer boardId) throws Exception{
        Board board = boardRepository.getById(boardId);
        User user = getById(userId).getBody();
        board.users.add(user);
        if(board.password.equals(""))
            board.usersWrite.add(user);
        else
            board.usersRead.add(user);
        boardRepository.save(board);
        user = getById(userId).getBody();
        return ResponseEntity.ok(user);
    }

    /**
     * Adds user to the list of users who have write access within a board
     * and also the other way around
     * @param userId
     * @param boardId
     * @return
     * @throws Exception
     */
    public ResponseEntity<User> joinBoardWriteAccess(Integer userId,
                                                     Integer boardId) throws Exception{
        Board board = boardRepository.getById(boardId);
        User user = getById(userId).getBody();
        board.usersWrite.add(user);
        boardRepository.save(board); //might cause problems
        user = getById(userId).getBody();
        return ResponseEntity.ok(user);
    }

    /**
     * Method which deletes the board from joined boards by the user
     * @param userId - id of the user
     * @param boardId - id of the board
     * @return
     * @throws Exception
     */
    public ResponseEntity<User> removeBoard(Integer userId, Integer boardId) throws Exception {
        Board board = boardRepository.getById(boardId);
        User user = getById(userId).getBody();

        board.users.remove(user);
        boardRepository.save(board);
        user = getById(userId).getBody();
        return ResponseEntity.ok(user);
    }

    public UserRepository getRepo(){
        return this.repo;

    }

}
