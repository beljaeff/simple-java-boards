package com.github.beljaeff.sjb.model;

import com.github.beljaeff.sjb.model.common.IdentifiedActiveEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REFRESH;
import static com.github.beljaeff.sjb.util.DateTimeUtils.DATE_TIME_PATTERN;

/*
 * Set fields for displaying topics with last post and author
 */
@NamedEntityGraph(name = EntityGraphs.TOPIC_WITH_LAST_POST_AND_AUTHOR,
    attributeNodes = {
        @NamedAttributeNode(value = "lastPost", subgraph = "postsWith.author"),
        @NamedAttributeNode(value = "author", subgraph = "usersWith.avatar")
    },
    subgraphs = {
        @NamedSubgraph(
            name = "postsWith.author",
            attributeNodes = @NamedAttributeNode(value = "author", subgraph = "usersWith.avatar")
        ),
        @NamedSubgraph(
            name = "usersWith.avatar",
            attributeNodes = @NamedAttributeNode("avatar")
        )
    }
)
/*
 * Set fields for displaying topic with board and author (single page)
 */
@NamedEntityGraph(name = EntityGraphs.TOPIC_WITH_BOARD_AND_AUTHOR,
    attributeNodes = {
        @NamedAttributeNode(value = "board"),
        @NamedAttributeNode(value = "author", subgraph = "usersWith.avatar")
    },
    subgraphs = {
        @NamedSubgraph(
            name = "usersWith.avatar",
            attributeNodes = @NamedAttributeNode("avatar")
        )
    }
)

@DynamicUpdate // Needed to properly generate position for topic during create topic
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "topics")
public class Topic extends IdentifiedActiveEntity {

    @NotBlank
    @Size(min = 2, max = 128)
    @Column(nullable = false)
    private String title;

    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_board", nullable = false)
    private Board board;

    @Column(name = "is_sticky", nullable = false)
    private boolean isSticky;

    @OneToOne(fetch = FetchType.LAZY, cascade = { PERSIST, MERGE, REFRESH, DETACH })
    @JoinColumn(name = "id_first_post")
    private Post firstPost;

    @OneToOne(fetch = FetchType.LAZY, cascade = { PERSIST, MERGE, REFRESH, DETACH })
    @JoinColumn(name = "id_last_post")
    private Post lastPost;

    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_author", nullable = false, updatable = false)
    private User author;

    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
    @Column(name = "date_create", nullable = false, insertable = false, updatable = false)
    private LocalDateTime dateCreate;

    @PositiveOrZero
    @Column(name = "posts_count", nullable = false)
    private long postsCount;

    @PositiveOrZero
    @Column(name = "views_count", nullable = false)
    private long viewsCount;

    @NotBlank
    @Size(max = 32)
    private String icon;

    @Column(name = "is_locked", nullable = false)
    private boolean isLocked;

    @Column(name = "is_approved", nullable = false)
    private boolean isApproved;

    @OrderBy("isSticky DESC, dateCreate")
    @OneToMany(mappedBy = "topic")
    private List<Post> posts;
}