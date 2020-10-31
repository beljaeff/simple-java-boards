package com.github.beljaeff.sjb.model;

import com.github.beljaeff.sjb.model.common.NamedEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * There are some standard groups:
 *  - ALL - unregistered users
 *  - Registered users
 *  - Privileged users
 *  - Moderators - global moderators for all boards
 *  - Admins
 * Also it is possible to add additional groups with its own permissions
 */

@NamedEntityGraph(name = EntityGraphNamesHelper.GROUPS_WITH_PERMISSIONS,
        attributeNodes = @NamedAttributeNode(value = "permissions")
)

@NamedEntityGraph(name = EntityGraphNamesHelper.GROUPS_WITH_OWNERS,
        attributeNodes = @NamedAttributeNode(value = "owners")
)

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "groups")
public class Group extends NamedEntity {

    @NotBlank
    @Size(min = 2, max = 32)
    private String code;

    @NotBlank
    @Pattern(regexp = "^[0-9a-fA-F]{6}$")
    private String color;

    @Size(max = 128)
    private String description;

    /**
     * Group with minimum weight shows first
     */
    @Column(nullable = false)
    private int weight;

    @ManyToMany
    @JoinTable(name = "group_permissions",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions;

    @ManyToMany
    @JoinTable(name = "user_groups",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> owners;
}