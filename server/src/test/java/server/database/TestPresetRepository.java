package server.database;

import commons.Preset;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestPresetRepository implements PresetRepository{

    public static final String FIND_ALL = "Find All";
    public static final String FIND_BY_ID = "Find By Id";
    public static final String EXISTS_BY_ID = "Exists By Id";
    public static final String SAVE = "Save";
    public static final String DELETE_BY_ID = "Delete By Id";

    @Override
    public void updatePresetBackgroundById(Integer id, String color) {

    }

    @Override
    public void updatePresetFontById(Integer id, String font) {

    }

    @Override
    public List<Preset> findAll() {
        return null;
    }

    @Override
    public List<Preset> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Preset> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Preset> findAllById(Iterable<Integer> integers) {
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
    public void delete(Preset entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends Preset> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Preset> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Preset> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Preset> findById(Integer integer) {
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
    public <S extends Preset> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Preset> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Preset> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> integers) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Preset getOne(Integer integer) {
        return null;
    }

    @Override
    public Preset getById(Integer integer) {
        return null;
    }

    @Override
    public <S extends Preset> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Preset> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Preset> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Preset> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Preset> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Preset> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Preset, R> R findBy(Example<S> example,
                                          Function<FluentQuery.FetchableFluentQuery<S>, R>
                                                  queryFunction) {
        return null;
    }
}
