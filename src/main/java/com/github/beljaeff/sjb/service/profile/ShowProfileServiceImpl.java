package com.github.beljaeff.sjb.service.profile;

import com.github.beljaeff.sjb.exception.ForbiddenException;
import com.github.beljaeff.sjb.service.security.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.beljaeff.sjb.dto.dto.profile.ProfileDto;
import com.github.beljaeff.sjb.mapper.ProfileMapper;
import com.github.beljaeff.sjb.model.User;
import com.github.beljaeff.sjb.service.conversation.TopicService;

import java.util.Collections;

import static com.github.beljaeff.sjb.enums.ErrorCode.ACCESS_TO_SECURITY_SECTION_NOT_ALLOWED;
import static com.github.beljaeff.sjb.util.UserUtils.getCurrentUser;

@Slf4j
@Service
public class ShowProfileServiceImpl implements ShowProfileService {

    private final UserService userService;
    private final TopicService topicService;

    private final ProfileMapper profileMapper;

    @Autowired
    public ShowProfileServiceImpl(UserService userService, TopicService topicService, ProfileMapper profileMapper) {
        this.userService = userService;
        this.topicService = topicService;
        this.profileMapper = profileMapper;
    }

    private User getUser(int userId) {
        User user = getCurrentUser().getUser();
        if (userId != user.getId()) {
            user = userService.get(userId);
        }
        return user;
    }

    @Override
    public ProfileDto getOverview(int userId) {
        return profileMapper.userToOverviewDto(getUser(userId));
    }

    @Override
    public ProfileDto getSecurity(int userId) {
        if (userId != getCurrentUser().getUser().getId()) {
            log.error("Requested account id '{}' does not match current user account id '{}'", userId, getCurrentUser().getUser().getId());
            throw new ForbiddenException(Collections.singletonList(ACCESS_TO_SECURITY_SECTION_NOT_ALLOWED));
        }
        return profileMapper.userToSecurityDto(getUser(userId));
    }

    @Override
    public ProfileDto getGroups(int userId) {
        return profileMapper.userToGroupsDto(userService.getWithGroups(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileDto getStatistics(int userId) {
        return profileMapper.userToStatisticsDto(getUser(userId), topicService.getTopicsCountForUser(userId));
    }
}
