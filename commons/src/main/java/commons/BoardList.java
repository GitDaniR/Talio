package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;
@Entity
public class BoardList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer id;

    public String title;

    @ManyToOne
    @JoinColumn(name = "board_id", referencedColumnName = "id")
    public Board board;

    public int index;
    @OneToMany(
            mappedBy = "list",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    @JsonIgnore
    public List<Card> cards = new ArrayList<>();

    private BoardList(){}

    public BoardList(String title, Board board) {
        this.title = title;
        this.board = board;
    }

    public BoardList(String text) {
        this.title = text;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public void addCard(Card card){
        this.cards.add(card);
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
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }


}
