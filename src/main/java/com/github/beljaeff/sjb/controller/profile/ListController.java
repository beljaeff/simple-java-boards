package com.github.beljaeff.sjb.controller.profile;

import com.github.beljaeff.sjb.service.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.github.beljaeff.sjb.controller.common.BaseController;

import static com.github.beljaeff.sjb.controller.RoutesHelper.PROFILE_LIST;

@Controller
@PreAuthorize("hasAnyPermissions('VIEW', 'ADMIN')")
public class ListController extends BaseController {

    private static final String PROFILE_LIST_TPL = "profile/list";

    private final UserService userService;

    @Autowired
    public ListController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(PROFILE_LIST)
    public String getList(@RequestParam(defaultValue = "1") int page, Model model) {
        model.addAttribute(ATTR_PAGE, userService.getList(page));
        return PROFILE_LIST_TPL;
    }
}