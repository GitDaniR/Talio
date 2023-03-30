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
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    public Integer id;
    public String title;
    public String description;

    @ManyToMany
    @JoinTable(
        name = "cardHasTag",
        joinColumns = @JoinColumn(name = "card_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    public List<Tag> tags = new ArrayList<>();

    @OneToMany(
        mappedBy = "card",
        cascade = CascadeType.ALL
    )
    public List<Subtask> subtasks = new ArrayList<>();

    public int index;
    @ManyToOne
    @JoinColumn(name = "listId", insertable = false, updatable = false)
    @JsonIgnore
    public BoardList list;

    public Integer listId;

    public Card(String title, String description, int index, BoardList list, Integer listId) {
        this.title = title;
        this.description = description;
        this.index = index;
        this.list = list;
        this.listId = listId;
    }
    public Card(Integer id, String title, String description,
                int index, BoardList list, int listId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.index = index;
        this.list = list;
        this.listId = listId;
    }

    public Card() {}

    public Integer getId() {
        return id;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setList(BoardList list) {
        this.list = list;
        this.listId = list.getId();
    }

    public void addTag(Tag tag) {
        if(!tags.contains(tag)) tags.add(tag);
    }

    public void removeTag(Tag tag){
        tags.remove(tag);
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
