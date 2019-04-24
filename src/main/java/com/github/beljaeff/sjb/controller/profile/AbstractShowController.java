package com.github.beljaeff.sjb.controller.profile;

import com.github.beljaeff.sjb.dto.dto.profile.ProfileDto;
import com.github.beljaeff.sjb.enums.ErrorCode;
import com.github.beljaeff.sjb.enums.ProfileSection;
import com.github.beljaeff.sjb.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import com.github.beljaeff.sjb.controller.common.BaseController;
import com.github.beljaeff.sjb.dto.dto.BreadcrumbDto;

import java.util.Collections;
import java.util.List;

abstract public class AbstractShowController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(AbstractShowController.class);

    private static final String PROFILE_VIEW_TPL = "profile/view";

    private static final String ATTR_SECTION = "profileSection";
    private static final String ATTR_USER    = "user";
    private static final String ATTR_HEADER  = "header";

    abstract protected List<BreadcrumbDto> getBreadcrumbs(ProfileDto dto);

    abstract protected String getHeader(ProfileDto dto);

    protected String showProfile(ProfileDto dto, ProfileSection section, Model model) {
        if(dto == null) {
            log.error("Can not show null profile (null ProfileDto given)");
            throw new NotFoundException(Collections.singletonList(ErrorCode.PROFILE_NOT_FOUND));
        }
        dto.setProfileSection(section);
        model.addAttribute(ATTR_HEADER, getHeader(dto));
        model.addAttribute(ATTR_USER, dto);
        model.addAttribute(ATTR_SECTION, section.getCode());
        model.addAttribute(ATTR_BREADCRUMBS, getBreadcrumbs(dto));
        return PROFILE_VIEW_TPL;
    }
}