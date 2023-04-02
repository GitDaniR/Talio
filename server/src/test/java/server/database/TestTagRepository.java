package server.database;

import commons.Tag;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestTagRepository implements TagRepository{
    public static final String FIND_BY_ID = "Find By Id";
    public static final String EXISTS_BY_ID = "Exists By Id";
    public static final String SAVE = "Save";
    public static final String DELETE_BY_ID = "Delete By Id";
    private List<String> calls;
    private List<Tag> tags;

    public TestTagRepository() {
        this.calls = new ArrayList<>();
        this.tags = new ArrayList<>();
    }

    public void setTags(List<Tag> tags){
        this.tags = tags;
    }

    public List<Tag> getTags(){
        return tags;
    }

    public void call(String method) {
        this.calls.add(method);
    }

    public List<String> getCalls(){
        return calls;
    }

    @Override
    public List<Tag> findAll() {
        return tags;
    }

    @Override
    public List<Tag> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Tag> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Tag> findAllById(Iterable<Integer> integers) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer integer) {
        call(DELETE_BY_ID);
        for(Tag t: tags){
            if(t.id == integer) tags.remove(t);
        }
    }


    @Override
    public void delete(Tag entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends Tag> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Tag> S save(S entity) {
        call(SAVE);
        for(int i = 0; i < tags.size(); i++){
            if(tags.get(i).id == entity.id){
                tags.set(i, entity);
                return (S) tags.get(i);
            }
        }
        tags.add(entity);
        return (S) tags.get(tags.size() - 1);
    }


    @Override
    public <S extends Tag> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Tag> findById(Integer integer) {
        call(FIND_BY_ID);
        for(Tag t: tags){
            if(t.id == integer) return Optional.of(t);
        }
        return Optional.empty();
    }


    @Override
    public boolean existsById(Integer integer) {
        call(EXISTS_BY_ID);
        for(Tag t: tags){
            if(t.id == integer) return true;
        }
        return false;
    }


    @Override
    public void flush() {

    }

    @Override
    public <S extends Tag> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Tag> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Tag> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> integers) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Tag getOne(Integer integer) {
        return null;
    }

    @Override
    public Tag getById(Integer integer) {
        return null;
    }

    @Override
    public <S extends Tag> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Tag> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Tag> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Tag> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Tag> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Tag> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Tag, R> R findBy(Example<S> example,
                                       Function<FluentQuery.FetchableFluentQuery<S>,
                                           R> queryFunction) {
        return null;
    }
}
