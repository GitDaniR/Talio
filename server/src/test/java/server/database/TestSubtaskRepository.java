package server.database;

import commons.BoardList;
import commons.Subtask;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestSubtaskRepository implements SubtaskRepository{
    public static final String FIND_BY_ID = "Find By Id";
    public static final String EXISTS_BY_ID = "Exists By Id";
    public static final String SAVE = "Save";
    public static final String DELETE_BY_ID = "Delete By Id";
    public static final String SHIFT_DOWN = "Shift Down";
    private List<String> calls;
    private List<Subtask> subtasks;

    public TestSubtaskRepository() {
        this.calls = new ArrayList<>();
        this.subtasks = new ArrayList<>();
    }

    public void setSubtasks(List<Subtask> subtasks){
        this.subtasks = subtasks;
    }

    public List<Subtask> getSubtasks(){
        return subtasks;
    }

    public void call(String method) {
        this.calls.add(method);
    }

    public List<String> getCalls(){
        return calls;
    }

    @Override
    public void shiftSubtasksDown(int index, int cardId) {
        call(SHIFT_DOWN);
        for(Subtask s: subtasks){
            if(s.cardId == cardId && s.index > index) s.index--;
        }
    }

    @Override
    public List<Subtask> findAll() {
        return null;
    }

    @Override
    public List<Subtask> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Subtask> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Subtask> findAllById(Iterable<Integer> integers) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer integer) {
        call(DELETE_BY_ID);
        for(Subtask s: subtasks){
            if(s.id == integer) subtasks.remove(s);
        }
    }

    @Override
    public void delete(Subtask entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends Subtask> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Subtask> S save(S entity) {
        call(SAVE);
        for(int i = 0; i < subtasks.size(); i++){
            if(subtasks.get(i).id == entity.id){
                subtasks.set(i, entity);
                return (S) subtasks.get(i);
            }
        }
        subtasks.add(entity);
        return (S) subtasks.get(subtasks.size() - 1);
    }

    @Override
    public <S extends Subtask> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Subtask> findById(Integer integer) {
        call(FIND_BY_ID);
        for(Subtask s: subtasks){
            if(s.id == integer) return Optional.of(s);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer integer) {
        call(EXISTS_BY_ID);
        for(Subtask s: subtasks){
            if(s.id == integer) return true;
        }
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Subtask> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Subtask> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Subtask> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> integers) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Subtask getOne(Integer integer) {
        return null;
    }

    @Override
    public Subtask getById(Integer integer) {
        return null;
    }

    @Override
    public <S extends Subtask> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Subtask> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Subtask> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Subtask> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Subtask> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Subtask> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Subtask, R> R findBy(Example<S> example,
                                           Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
