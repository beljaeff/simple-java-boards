package com.github.beljaeff.sjb.repository.condition;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.github.beljaeff.sjb.model.Group;

import java.util.HashSet;
import java.util.Set;

//TODO: tests for setters
@Getter
@NoArgsConstructor
@Builder
public class UserCondition implements Condition {

    @Setter
    private Boolean isActive;

    @Setter
    private Boolean isActivated;

    @Setter
    private Boolean isBanned;

    @Setter
    private Boolean showAnonymous;

    private String nickName;

    @Setter
    private String email;

    private String validationCode;

    /*
     * if true => criteriaBuilder.or(..), false => criteriaBuilder.and(..)
     */
    private boolean joinWithOr;

    private Set<Group> groups;

    public UserCondition(int[] groups) {
        setGroups(groups);
    }

    public UserCondition(Set<Group> groups) {
        setGroups(groups);
    }

    public UserCondition(Boolean isActive, Boolean isActivated, Boolean isBanned, Boolean showAnonymous,
                         String nickName, String email, String validationCode, boolean joinWithOr,
                         Set<Group> groups) {
        this(groups);
        this.isActive = isActive;
        this.isActivated = isActivated;
        this.isBanned = isBanned;
        this.showAnonymous = showAnonymous;
        this.nickName = nickName;
        this.email = email;
        this.validationCode = validationCode;
        this.joinWithOr = joinWithOr;
    }

    public void setGroups(Set<Group> groups) {
        if(groups == null) {
            return;
        }
        this.groups = new HashSet<>();
        for(Group group : groups) {
            if(group == null) {
                continue;
            }
            Group g = new Group();
            g.setId(group.getId());
            this.groups.add(g);
        }
    }

    public void setGroups(int[] groups) {
        if(groups == null) {
            return;
        }
        this.groups = new HashSet<>();
        for(int i : groups) {
            Group g = new Group();
            g.setId(i);
            this.groups.add(g);
        }
    }
}