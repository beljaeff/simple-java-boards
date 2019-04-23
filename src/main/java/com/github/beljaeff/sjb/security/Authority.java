package com.github.beljaeff.sjb.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@Getter
public class Authority implements GrantedAuthority {
    public static final String DEFAULT_PREFIX = "ROLE_";

    private final String authority;
}
