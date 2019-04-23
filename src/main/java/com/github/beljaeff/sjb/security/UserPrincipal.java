package com.github.beljaeff.sjb.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;
import com.github.beljaeff.sjb.model.Permission;
import com.github.beljaeff.sjb.model.User;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.beljaeff.sjb.util.UserUtils.mapAuthorities;

//TODO: create fields for user profile
@Getter
@Setter
public class UserPrincipal extends org.springframework.security.core.userdetails.User {
    private static final long serialVersionUID = 1L;

    private int id;

    private User user;

    private Set<String> permissions;

    public UserPrincipal(User user) {
        //TODO: implement case when user register account but not activate it in time (credentialNonExpired)
        super(user.getNickName(), user.getPassword(),
              user.getIsActive() && user.getIsActivated(), true, true, !user.getIsBanned(),
              mapAuthorities(user.getGroups()));
        this.user = user;
        this.id = user.getId();
        this.permissions = fillPermissions();
    }

    private Set<String> fillPermissions() {
        if(CollectionUtils.isEmpty(user.getGroups())) {
            return Collections.emptySet();
        }
        return user
            .getGroups()
            .stream()
            .filter(Objects::nonNull)
            .flatMap(g -> g.getPermissions().stream())
            .filter(Objects::nonNull)
            .map(Permission::getCode)
            .collect(Collectors.toSet());
    }
}