package server.database;

import commons.Card;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestCardRepository implements CardRepository{

    public static final String SHIFT_CARDS_RIGHT = "Shift Cards Right";
    public static final String SHIFT_CARDS_LEFT = "Shift Cards Left";
    public static final String FIND_ALL = "Find All";
    public static final String FIND_BY_ID = "Find By Id";
    public static final String EXISTS_BY_ID = "Exists By Id";
    public static final String SAVE = "Save";
    public static final String DELETE_BY_ID = "Delete By Id";

    private List<String> calls;
    private List<Card> cards;

    public TestCardRepository(){
        calls = new ArrayList<>();
        cards = new ArrayList<>();
    }

    public void call(String method){
        calls.add(method);
    }

    public List<String> getCalls(){
        return calls;
    }

    public void setCards(List<Card> cards){
        this.cards = cards;
    }
    @Override
    public void shiftCardsRight(int index, int listId) {
        call(SHIFT_CARDS_RIGHT);
        for(Card c: cards){
            if(c.listId == listId && c.index >= index){
                c.index ++;
            }
        }
    }

    @Override
    public void shiftCardsLeft(int index, int listId) {
        call(SHIFT_CARDS_LEFT);
        for(Card c: cards){
            if(c.listId == listId && c.index > index){
                c.index --;
            }
        }
    }

    @Override
    public <S extends Card> S save(S entity) {
        call(SAVE);
        int maxId = -1;
        for(Card c: cards){
            maxId = Math.max(maxId, c.id);
            if(entity.id != null && c.id == entity.id){
                cards.remove(c);
                cards.add(entity);
                return entity;
            }
        }
        entity.id = maxId + 1;
        cards.add(entity);
        return entity;
    }

    @Override
    public List<Card> findAll() {
        call(FIND_ALL);
        return cards;
    }

    @Override
    public Optional<Card> findById(Integer integer) {
        calls.add(FIND_BY_ID);
        for(Card c: cards){
            if(c.id == integer) return Optional.of(c);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer integer) {
        calls.add(EXISTS_BY_ID);
        for(Card c: cards){
            if(c.id == integer) return true;
        }
        return false;
    }

    @Override
    public void deleteById(Integer integer) {
        call(DELETE_BY_ID);
        for(Card c: cards){
            if(c.id == integer){
                cards.remove(c);
            }
        }
    }

    @Override
    public List<Card> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Card> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Card> findAllById(Iterable<Integer> integers) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void delete(Card entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends Card> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Card> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Card> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Card> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Card> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> integers) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Card getOne(Integer integer) {
        return null;
    }

    @Override
    public Card getById(Integer integer) {
        return null;
    }

    @Override
    public <S extends Card> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Card> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Card> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Card> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Card> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Card> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Card, R> R findBy(Example<S> example,
                                        Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
