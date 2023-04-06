package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer id;

    public String username;

    @ManyToMany(
            mappedBy = "users",
            cascade = CascadeType.PERSIST
    )
    public List<Board> boards = new ArrayList<>();

    @ManyToMany(
            mappedBy = "usersWrite",
            cascade = CascadeType.PERSIST
    )
    public List<Board> unlockedBoards = new ArrayList<>();

    @ManyToMany(
            mappedBy = "usersRead",
            cascade = CascadeType.PERSIST
    )
    public List<Board> lockedBoards = new ArrayList<>();


    public User() {}

    public User(String username) {
        this.username = username;
    }

    public boolean hasBoardAlready(int id){
        for(Board b : boards)
            if(b.id==id)
                return true;
        return false;
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
