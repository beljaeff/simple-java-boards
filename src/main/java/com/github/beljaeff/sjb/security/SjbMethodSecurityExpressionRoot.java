package com.github.beljaeff.sjb.security;

import com.github.beljaeff.sjb.exception.PermissionsNotSetException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import java.util.Set;

public class SjbMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    @Getter
    @Setter
    private Object filterObject;

    @Getter
    @Setter
    private Object returnObject;

    private Object target;

    public SjbMethodSecurityExpressionRoot(Authentication a) {
        super(a);
    }

    /**
     * Sets the "this" property for use in expressions. Typically this will be the "this"
     * property of the {@code JoinPoint} representing the method invocation which is being
     * protected.
     *
     * @param target the target object on which the method in is being invoked.
     */
    public void setThis(Object target) {
        this.target = target;
    }

    public Object getThis() {
        return target;
    }

    public boolean hasPermission(String permission) {
        return hasAnyPermissions(permission);
    }

    public boolean hasAnyPermissions(String...permissions) {
        if(permissions == null || permissions.length == 0) {
            throw new PermissionsNotSetException("permissions should be set");
        }
        Set<String> actualPermissions = ((UserPrincipal) this.getPrincipal()).getPermissions();

        for (String permission : permissions) {
            if (actualPermissions.contains(permission)) {
                return true;
            }
        }

        return false;
    }
}