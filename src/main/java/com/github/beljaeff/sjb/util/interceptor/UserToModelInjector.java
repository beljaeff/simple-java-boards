package com.github.beljaeff.sjb.util.interceptor;

import com.github.beljaeff.sjb.mapper.UserMapper;
import com.github.beljaeff.sjb.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.github.beljaeff.sjb.dto.dto.UserDto;
import com.github.beljaeff.sjb.util.UserUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * Injects user and permissions to the model every requests managed
 */
@Slf4j
public class UserToModelInjector extends HandlerInterceptorAdapter {

    private UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && !modelAndView.isEmpty()) {
            UserPrincipal userPrincipal = UserUtils.getForInject();
            if (userPrincipal != null) {
                UserDto dto = userMapper.userToUserDto(userPrincipal.getUser());
                modelAndView.getModelMap().addAttribute("userPrincipal", dto);
                Set<String> permissions = userPrincipal.getPermissions();
                modelAndView.getModelMap().addAttribute("userPermissions", permissions);
                log.trace("User {} with permissions {} successfully injected into model", dto, permissions);
            }
        }
    }
}