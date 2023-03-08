package commons;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
   @ManyToMany(
           mappedBy = "cards",
           cascade = CascadeType.PERSIST
   )
    public List<Tag> tags = new ArrayList<>();

    @OneToMany(
            mappedBy = "card",
            cascade = CascadeType.PERSIST
    )
    public List<Subtask> subtasks;
    public int index;
    @ManyToOne
    @JoinColumn(name = "list_id")
    @JsonBackReference
    public BoardList list;

    private Card(){}

    public Card(String title, String description, int index, BoardList list) {
        this.title = title;
        this.description = description;
        this.index = index;
        this.list = list;
    }

    public void addTag(Tag tag){
        tags.add(tag);
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
