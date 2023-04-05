package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer id;
    public String title;
    public String color;

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    public List<Card> cards = new ArrayList<>();

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "boardId", insertable = false, updatable = false)
    public Board board;

    public Integer boardId;

    public Tag() {
    }

    public Tag(String title, String color, Board board, int boardId) {
        this.title = title;
        this.color = color;
        this.board = board;
        this.boardId = boardId;
    }

    public Tag(String title, String color) {
        this.title = title;
        this.color = color;
    }

    public void addToCard(Card card){
        if(!cards.contains(card)) cards.add(card);
    }
    public void removeFromCard(Card card){
        cards.remove(card);
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
        return this.title;
    }
}
