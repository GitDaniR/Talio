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
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer id;
    public String title;
    public String password;
    public String colorBoardBackground;
    public String colorBoardFont;
    public String colorListsBackground;
    public String colorListsFont;

    @OneToMany(
            mappedBy = "board",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    public List<BoardList> lists = new ArrayList<>();

    @OneToMany(
            mappedBy = "board",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    public List<Tag> tags = new ArrayList<>();
    @OneToMany(
            mappedBy = "board",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    public List<Preset> cardPresets = new ArrayList<>();
    @OneToOne
    public Preset defaultCardPreset;
    @ManyToMany
    @JoinTable(
            name = "boardIsJoinedByUser",
            joinColumns = @JoinColumn(name = "boardId"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    public List<User> users = new ArrayList<>();

    private Board(){}

    public Board(String title, String password) {
        this.title = title;
        this.password = password;
        setDefaultColors();
    }

    public Board(Integer id, String title, String password, List<BoardList> lists) {
        this(title, password);
        this.id = id;
        this.lists = lists;
        setDefaultColors();
    }

    private void setDefaultColors(){
        this.colorBoardBackground = "0xfaebd7";
        this.colorListsBackground = "0xe6e6fa";
        this.colorBoardFont = "0x000000";
        this.colorListsFont = "0x000000";
    }

    public void setLists(List<BoardList> lists) {
        this.lists = lists;
    }

    public void addBoardList(BoardList boardList) {
        this.lists.add(boardList);
    }

    public void addCardPreset(Preset preset){this.cardPresets.add(preset);}
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
