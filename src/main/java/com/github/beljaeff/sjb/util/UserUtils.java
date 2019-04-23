package com.github.beljaeff.sjb.util;

import com.github.beljaeff.sjb.enums.BasePermission;
import com.github.beljaeff.sjb.exception.NotAuthenticatedException;
import com.github.beljaeff.sjb.model.Group;
import com.github.beljaeff.sjb.security.Authority;
import com.github.beljaeff.sjb.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class UserUtils {

    public static final int ANONYMOUS_ID = 0;
    public static final int ADMIN_ID = 1;

    //TODO: tests
    public static Set<Authority> mapAuthorities(Set<Group> groups) {
        if(CollectionUtils.isEmpty(groups)) {
            return Collections.emptySet();
        }
        return groups
                .stream()
                .filter(Objects::nonNull)
                .filter(Group::getIsActive)
                .map(Group::getCode)
                .map(String::toUpperCase)
                .map(Authority.DEFAULT_PREFIX::concat)
                .map(Authority::new)
                .collect(Collectors.toSet());
    }

    public static UserPrincipal getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            log.error("Can not get authorized principal");
            throw new NotAuthenticatedException();
        }
        return (UserPrincipal) authentication.getPrincipal();
    }

    public static Set<String> getCurrentUserPermissions() {
        return getCurrentUser().getPermissions();
    }

    public static boolean hasPermission(BasePermission permission) {
        Set<String> permissions = getCurrentUserPermissions();
        return permission != null && permissions != null && permissions.contains(permission.name());
    }

    public static UserPrincipal getForInject() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        return (principal instanceof UserPrincipal) ? (UserPrincipal) principal : null;
    }

    public static boolean isAnonymous() {
        return getCurrentUser().getId() == ANONYMOUS_ID;
    }
}