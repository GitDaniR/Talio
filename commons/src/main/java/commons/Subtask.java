package commons;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Subtask {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer id;

    public boolean done;

    public String title;
    public int index;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "cardId", insertable = false, updatable = false)
    public Card card;

    public Integer cardId;

    public Subtask(String title, boolean done, int index, Card card) {
        this.done = done;
        this.title = title;
        this.index = index;
        this.card = card;
        this.cardId = card.id;
    }

    public Subtask(String title, boolean done, int index, Card card, Integer cardId) {
        this.done = done;
        this.title = title;
        this.index = index;
        this.card = card;
        this.cardId = cardId;
    }
    public void setDone(boolean done) {
        this.done = done;
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
