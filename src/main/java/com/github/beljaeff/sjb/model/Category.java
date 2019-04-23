package com.github.beljaeff.sjb.model;

import com.github.beljaeff.sjb.model.common.PositionedEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Set fields for displaying categorized boards list
 */
@NamedEntityGraph(name = EntityGraphs.CATEGORIES_WITH_BOARDS,
    attributeNodes = @NamedAttributeNode(value = "boards", subgraph = "boardsWith.lastTopic"),
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
@Table(name = "categories")
public class Category extends PositionedEntity {

    @NotBlank
    @Size(min = 2, max = 128)
    @Column(nullable = false)
    private String title;

    @OrderBy("position, title")
    @OneToMany(mappedBy="category")
    private List<Board> boards;
}
