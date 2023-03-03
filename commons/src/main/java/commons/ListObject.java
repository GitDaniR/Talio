package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class ListObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer id;

    public String title;
    @ManyToOne
    public Board board;

    public int index;
    @OneToMany(
            mappedBy = "list",
            cascade = CascadeType.ALL,
            orphanRemoval = true

    )
    public List<Card> cards;

    private ListObject(){}

    public ListObject(String title, Board board) {
        this.title = title;
        this.board = board;
    }

    public void setCard(List<Card> card) {
        this.cards = card;
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
