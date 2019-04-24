package com.github.beljaeff.sjb.controller.profile;

import com.github.beljaeff.sjb.dto.dto.profile.ProfileDto;
import com.github.beljaeff.sjb.enums.ProfileSection;
import com.github.beljaeff.sjb.service.profile.ShowProfileService;
import com.github.beljaeff.sjb.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.github.beljaeff.sjb.dto.dto.BreadcrumbDto;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.github.beljaeff.sjb.controller.Routes.PROFILE_LIST;
import static com.github.beljaeff.sjb.controller.Routes.USER_PROFILE;
import static com.github.beljaeff.sjb.controller.Routes.USER_PROFILE_COMMON;
import static com.github.beljaeff.sjb.controller.Routes.USER_PROFILE_OVERVIEW;

@Controller
@PreAuthorize("(hasPermission('VIEW_PROFILE') && hasPermission('VIEW')) || hasPermission('ADMIN')")
public class ShowController extends AbstractShowController {

    private final ShowProfileService showProfileService;

    @Autowired
    public ShowController(ShowProfileService showProfileService) {
        this.showProfileService = showProfileService;
    }

    @Override
    public String getHeader(ProfileDto dto) {
        String messageCode = dto.getId() == UserUtils.getCurrentUser().getId() ? "own.profile.text.header" : "profile.text.header";
        return recordService.getText(messageCode, dto.getNickName()) +
               recordService.getText(dto.getProfileSection().getCode() + ".profile.section.header.add");
    }

    @Override
    public List<BreadcrumbDto> getBreadcrumbs(ProfileDto dto) {
        List<BreadcrumbDto> breadcrumbs = super.getBreadcrumbs();
        breadcrumbs.add(new BreadcrumbDto(PROFILE_LIST, recordService.getText("user.profiles.text.header")));
        breadcrumbs.add(new BreadcrumbDto(buildSegmentsPath(USER_PROFILE_COMMON, dto.getId(), dto.getProfileSection().getCode()), getHeader(dto)));
        return breadcrumbs;
    }

    @GetMapping(USER_PROFILE)
    public String show(@PathVariable int id) {
        return "redirect:" + buildSegmentsPath(USER_PROFILE_OVERVIEW, id);
    }

    @GetMapping(USER_PROFILE_COMMON)
    public String show(@PathVariable int id, @NotNull @PathVariable ProfileSection section, Model model) {
        return showProfile(
            getContents(section, id),
            section,
            model
       );
    }

    //TODO: strategy pattern + global context for all urls
    private ProfileDto getContents(ProfileSection section, int userId) {
        switch (section) {
            case OVERVIEW:
                return showProfileService.getOverview(userId);
            case SECURITY:
                return showProfileService.getSecurity(userId);
            case GROUPS:
                return showProfileService.getGroups(userId);
            case STATISTICS:
                return showProfileService.getStatistics(userId);
            default:
                break;
        }
        return null;
    }
}