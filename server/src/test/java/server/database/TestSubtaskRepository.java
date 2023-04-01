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
    private List<String> calls;
    private List<Subtask> subtasks;

    public TestSubtaskRepository() {
        this.calls = new ArrayList<>();
        this.subtasks = new ArrayList<>();
    }

    public void setSubtasks(List<Subtask> subtasks){
        this.subtasks = subtasks;
    }

    public void call(String method) {
        this.calls.add(method);
    }

    @Override
    public void shiftSubtasksDown(int index, int cardId) {

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
        return null;
    }

    @Override
    public <S extends Subtask> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Subtask> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer integer) {
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
