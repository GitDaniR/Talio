package server.database;

import commons.Board;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

class TestBoardRepository implements BoardRepository{

    public static final String FIND_ALL = "Find All";
    public static final String FIND_BY_ID = "Find By Id";
    public static final String EXISTS_BY_ID = "Exists By Id";
    public static final String SAVE = "Save";
    public static final String DELETE_BY_ID = "Delete By Id";

    private List<String> calls;
    private List<Board> boards;

    public TestBoardRepository(){
        this.calls = new ArrayList<String>();
        this.boards = new ArrayList<Board>();
    }

    public void call(String method){
        calls.add(method);
    }

    public List<String> getCalls(){
        return calls;
    }

    public void setBoards(List<Board> boards){
        this.boards = boards;
    }

    public List<Board> getBoards(){
        return this.boards;
    }

    @Override
    public List<Board> findAll() {
        call(FIND_ALL);
        return this.boards;
    }

    @Override
    public List<Board> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Board> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Board> findAllById(Iterable<Integer> integers) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer integer) {
        call(DELETE_BY_ID);
        for (Board b : this.boards) {
            if (b.id == integer) {
                this.boards.remove(b);
            }
        }
    }

    @Override
    public void delete(Board entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends Board> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Board> S save(S entity) {
        call(SAVE);
        entity.id = this.boards.size();
        this.boards.add(entity);
        return entity;
    }

    @Override
    public <S extends Board> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Board> findById(Integer integer) {
        calls.add(FIND_BY_ID);
        for(Board b: this.boards){
            if(b.id == integer)
                return Optional.of(b);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer integer) {
        call(EXISTS_BY_ID);
        for (Board b : this.boards) {
            if (b.id == integer)
                return true;
        }
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Board> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Board> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Board> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> integers) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Board getOne(Integer integer) {
        return null;
    }

    @Override
    public Board getById(Integer integer) {
        return null;
    }

    @Override
    public <S extends Board> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Board> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Board> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Board> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Board> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Board> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Board, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}