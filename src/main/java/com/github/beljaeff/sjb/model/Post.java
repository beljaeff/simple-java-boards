package com.github.beljaeff.sjb.model;

import com.github.beljaeff.sjb.model.common.IdentifiedActiveEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

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
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REFRESH;
import static com.github.beljaeff.sjb.util.DateTimeUtils.DATE_TIME_PATTERN;

/*
 * Set fields for displaying posts with last post and author
 */
@NamedEntityGraph(name = EntityGraphNamesHelper.POST_EXCEPT_TOPIC_AND_CHILD_POSTS,
        attributeNodes = {
                @NamedAttributeNode(value = "parentPost"),
                @NamedAttributeNode(value = "author", subgraph = "usersWith.avatar"),
                @NamedAttributeNode(value = "userLastUpdate", subgraph = "usersWith.avatar")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "usersWith.avatar",
                        attributeNodes = @NamedAttributeNode("avatar")
                )
        }
)

@NamedEntityGraph(name = EntityGraphNamesHelper.POST_WITH_AUTHOR_AND_ATTACHMENTS,
        attributeNodes = {
                @NamedAttributeNode(value = "attachments"),
                @NamedAttributeNode(value = "author")
        }
)

@NamedEntityGraph(name = EntityGraphNamesHelper.POST_WITH_ATTACHMENTS_AND_TOPIC_WITH_BOARD,
        attributeNodes = {
                @NamedAttributeNode(value = "attachments"),
                @NamedAttributeNode(value = "topic", subgraph = "topicWith.board"),
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "topicWith.board",
                        attributeNodes = @NamedAttributeNode("board")
                )
        }
)

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity
@Table(name = "posts")
public class Post extends IdentifiedActiveEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_topic", nullable = false)
    private Topic topic;

    /**
     * Post current post answered for. Makes possible tree-based post print.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_parent")
    private Post parentPost;

    @OneToMany(mappedBy="parentPost")
    private Set<Post> childPosts;

    @Column(name = "is_sticky", nullable = false)
    private boolean isSticky;

    @NotBlank
    @Size(min = 7, max = 15)
    @Pattern(regexp = "^(([1-9]?[0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([1-9]?[0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$")
    @Column(name = "ip_create", nullable = false, updatable = false)
    private String ipCreate;

    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_author", nullable = false, updatable = false)
    private User author;

    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
    @Column(name = "date_create", nullable = false, insertable = false, updatable = false)
    private LocalDateTime dateCreate;

    @NotBlank
    @Size(min = 7, max = 15)
    @Pattern(regexp = "^(([1-9]?[0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([1-9]?[0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$")
    @Column(name = "ip_last_update")
    private String ipLastUpdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user_last_update")
    private User userLastUpdate;

    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
    @Column(name = "date_last_update", nullable = false, insertable = false)
    private LocalDateTime dateLastUpdate;

    @NotBlank
    @Size(max = 4096)
    private String body;

    @PositiveOrZero
    @Column(name = "karma_bad", nullable = false)
    private int badKarma;

    @PositiveOrZero
    @Column(name = "karma_good", nullable = false)
    private int goodKarma;

    @Column(name = "is_approved", nullable = false)
    private boolean isApproved;

    @ManyToMany(cascade = { PERSIST, MERGE, REFRESH, DETACH })
    @JoinTable(name = "post_attachments",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "attachment_id")
    )
    private List<Attachment> attachments;
}
