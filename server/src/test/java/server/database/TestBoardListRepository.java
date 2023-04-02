package server.database;

import commons.BoardList;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestBoardListRepository implements BoardListRepository{

    public static final String FIND_ALL = "Find All";
    public static final String FIND_BY_ID = "Find By Id";
    public static final String EXISTS_BY_ID = "Exists By Id";
    public static final String SAVE = "Save";
    public static final String DELETE_BY_ID = "Delete By Id";

    public static final String UPDATE_TITLE_BY_ID = "Update Title By Id";

    private List<String> calls;
    private List<BoardList> boardLists;

    public TestBoardListRepository() {
        this.calls = new ArrayList<>();
        this.boardLists = new ArrayList<>();
    }

    public void call(String method) {
        this.calls.add(method);
    }

    public List<String> getCalls() {
        return this.calls;
    }

    public void setBoardLists(List<BoardList> lists){
        this.boardLists = lists;
    }

    public List<BoardList> getBoardLists(){
        return this.boardLists;
    }

    @Override
    public void updateListById(Integer id, String title) {
        call(UPDATE_TITLE_BY_ID);
        for (BoardList b : this.boardLists) {
            if (b.id == id) {
                b.title = title;
                break;
            }
        }
    }

    @Override
    public List<BoardList> findAll() {
        call(FIND_ALL);
        return this.boardLists;
    }

    @Override
    public List<BoardList> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<BoardList> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<BoardList> findAllById(Iterable<Integer> integers) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer integer) {
        call(DELETE_BY_ID);
        for (BoardList b : this.boardLists) {
            if (b.id == integer) {
                this.boardLists.remove(b);
            }
        }
    }

    @Override
    public void delete(BoardList entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends BoardList> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends BoardList> S save(S entity) {
        call(SAVE);
        entity.id = this.boardLists.size();
        this.boardLists.add(entity);
        return entity;
    }

    @Override
    public <S extends BoardList> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<BoardList> findById(Integer integer) {
        calls.add(FIND_BY_ID);
        for(BoardList b: this.boardLists){
            if(b.id == integer)
                return Optional.of(b);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer integer) {
        call(EXISTS_BY_ID);
        for (BoardList b : this.boardLists) {
            if (b.id == integer)
                return true;
        }
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends BoardList> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends BoardList> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<BoardList> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> integers) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public BoardList getOne(Integer integer) {
        return null;
    }

    @Override
    public BoardList getById(Integer integer) {
        return null;
    }

    @Override
    public <S extends BoardList> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends BoardList> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends BoardList> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends BoardList> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends BoardList> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends BoardList> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends BoardList, R> R findBy(Example<S> example,
        Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
