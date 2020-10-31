package com.github.beljaeff.sjb.model;

import com.github.beljaeff.sjb.model.common.PositionedEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;


//TODO: tests for all entity graphs, checks retrieve fields
/*
 * Set fields for displaying categorized boards list
 */
@NamedEntityGraph(name = EntityGraphNamesHelper.BOARDS_WITH_LAST_TOPIC,
    attributeNodes = @NamedAttributeNode(value = "lastTopic", subgraph = "topicsWith.author"),
    subgraphs = {
        @NamedSubgraph(
            name = "topicsWith.author",
            attributeNodes = @NamedAttributeNode(value = "author", subgraph = "usersWith.avatar")
        ),
        @NamedSubgraph(
            name = "usersWith.avatar",
            attributeNodes = @NamedAttributeNode("avatar")
        )
    }
)

/*
 * Set fields for selecting one board with child boards
 */
@NamedEntityGraph(name = EntityGraphNamesHelper.BOARD_WITH_CHILD_BOARDS,
    attributeNodes = @NamedAttributeNode(value = "childBoards", subgraph = "boardsWith.lastTopic"),
    subgraphs = {
        @NamedSubgraph(
            name = "boardsWith.lastTopic",
            attributeNodes = @NamedAttributeNode(value="lastTopic", subgraph = "topicsWith.author")
        ),
        @NamedSubgraph(
            name = "topicsWith.author",
            attributeNodes = @NamedAttributeNode(value = "author", subgraph = "usersWith.avatar")
        ),
        @NamedSubgraph(
            name = "usersWith.avatar",
            attributeNodes = @NamedAttributeNode("avatar")
        )
    }
)

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "boards")
public class Board extends PositionedEntity {

    @NotBlank
    @Size(min = 2, max = 128)
    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_parent")
    private Board parentBoard;

    @OrderBy("position, title")
    @OneToMany(mappedBy="parentBoard")
    private List<Board> childBoards;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_category")
    private Category category;

    @Size(max = 512)
    private String description;

    @NotBlank
    @Size(max = 32)
    private String icon;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_last_topic")
    private Topic lastTopic;

    @Column(name = "topics_count", nullable = false)
    @PositiveOrZero
    private long topicsCount;

    @Column(name = "posts_count", nullable = false)
    @PositiveOrZero
    private long postsCount;

    @OrderBy("isSticky DESC")
    @OneToMany(mappedBy = "board")
    private List<Topic> topics;

    @ManyToMany
    @JoinTable(name = "board_moderators",
            joinColumns = @JoinColumn(name = "board_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> moderators;

    /**
     * Only these groups allowed work with current board
     */
    @ManyToMany
    @JoinTable(name = "board_groups",
            joinColumns = @JoinColumn(name = "board_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Set<Group> allowedGroups;
}
