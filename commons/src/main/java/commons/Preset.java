package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
public class Preset {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer id;
    public String backgroundColor;
    public String font;
    public String name;

    @OneToMany(
            mappedBy = "preset",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    public List<Card> cards = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "boardId", insertable = false, updatable = false)
    @JsonIgnore
    public Board board;
    public Integer boardId;

    public Preset(){}

    public Preset(String backgroundColor, String font,
                  List<Card> cards, Board board, Integer boardId) {
        this.backgroundColor = backgroundColor;
        this.font = font;
        this.cards = cards;
        this.board = board;
        this.boardId = boardId;
    }

    public Preset(Integer id, String backgroundColor, String font, Board board) {
        this.id = id;
        this.backgroundColor = backgroundColor;
        this.font = font;
        this.board = board;
        if(!(board == null))
            this.boardId = board.id;
    }

    public Preset(Integer id, String backgroundColor,
                  String font, List<Card> cards, Board board,
                  int boardId) {
        this.id = id;
        this.backgroundColor = backgroundColor;
        this.font = font;
        this.cards = cards;
        this.board = board;
        this.boardId = boardId;
    }

    public Preset(Integer id, String backgroundColor,
                  String font, List<Card> cards, String name, Board board,
                  int boardId) {
        this.id = id;
        this.backgroundColor = backgroundColor;
        this.font = font;
        this.cards = cards;
        this.board = board;
        this.name = name;
        this.boardId = boardId;
    }

    public Preset(String backgroundColor, String font,
                  List<Card> cards, String name,
                  Board board, int boardId) {
        this.backgroundColor = backgroundColor;
        this.font = font;
        this.cards = cards;
        this.board = board;
        this.name = name;
        this.boardId = boardId;
    }

    public Integer getId(){return this.id;}

    public String getName() {
        return this.name;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public void addCard(Card card){
        if(!this.cards.contains(card))
            this.cards.add(card);
    }

    public void removeCard(Card card) {
        this.cards.remove(card);
    }

    public void setBoard(Board board) {
        this.board = board;
        this.boardId = board.id;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return this.getName();
    }
}

