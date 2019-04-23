package com.github.beljaeff.sjb.security;

import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;
import com.github.beljaeff.sjb.service.security.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;

public class AnonymousAuthFilter extends GenericFilterBean implements InitializingBean {

    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();

    private String key;

    @Getter
    private Object principal;

    @Getter
    private Collection<GrantedAuthority> authorities;

    private UserService service;

    /**
     * Creates a filter with an anonymous principal from database
     *
     * @param key the key to identify tokens created by this filter
     * @param service the service to get anonymous user entity
     */
    public AnonymousAuthFilter(String key, UserService service) {
        Assert.hasLength(key, "key cannot be null or empty");
        Assert.notNull(service, "User service cannot be null");

        this.key = key;
        this.service = service;

        retrievePrincipalWithAuthorities();
    }

    // ~ Methods
    // ========================================================================================================

    @Override
    public void afterPropertiesSet() {
        Assert.hasLength(key, "key must have length");
        Assert.notNull(principal, "Anonymous authentication principal must be set");
        Assert.notNull(authorities, "Anonymous authorities must be set");
    }

    protected void retrievePrincipalWithAuthorities() {
        UserPrincipal anonymous = service.getAnonymousUser();
        this.principal = anonymous;
        this.authorities = anonymous.getAuthorities();
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            authentication = createAuthentication((HttpServletRequest) req);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.debug("Populated SecurityContextHolder with anonymous token: '" + authentication + "'");
        }
        else {
            logger.debug("SecurityContextHolder not populated with anonymous token, as it already contained: '" + authentication + "'");
        }

        chain.doFilter(req, res);
    }

    protected Authentication createAuthentication(HttpServletRequest request) {
        AnonymousAuthenticationToken auth = new AnonymousAuthenticationToken(key, principal, authorities);
        auth.setDetails(authenticationDetailsSource.buildDetails(request));

        return auth;
    }

    public void setAuthenticationDetailsSource(AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
        Assert.notNull(authenticationDetailsSource,"AuthenticationDetailsSource required");
        this.authenticationDetailsSource = authenticationDetailsSource;
    }
}
