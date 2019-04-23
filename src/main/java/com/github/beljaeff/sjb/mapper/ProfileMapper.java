package com.github.beljaeff.sjb.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import com.github.beljaeff.sjb.dto.dto.profile.GroupDto;
import com.github.beljaeff.sjb.dto.dto.profile.GroupsDto;
import com.github.beljaeff.sjb.dto.dto.profile.OverviewDto;
import com.github.beljaeff.sjb.dto.dto.profile.ProfileDto;
import com.github.beljaeff.sjb.dto.dto.profile.SecurityDto;
import com.github.beljaeff.sjb.dto.dto.profile.StatisticsDto;
import com.github.beljaeff.sjb.dto.form.profile.EditOverviewForm;
import com.github.beljaeff.sjb.model.Group;
import com.github.beljaeff.sjb.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class ProfileMapper extends AbstractMapper {

    private AttachmentMapper attachmentMapper;

    @Autowired
    public void setAttachmentMapper(AttachmentMapper attachmentMapper) {
        this.attachmentMapper = attachmentMapper;
    }

    private void mapBase(ProfileDto dto, User user) {
        if(!isLoaded(user) || user == null) {
            return;
        }
        dto.setId(user.getId());
        dto.setNickName(user.getNickName());
        if(isLoaded(user.getAvatar())) {
            dto.setAvatar(attachmentMapper.attachmentToAttachmentDto(user.getAvatar()));
        }
        dto.setIsBanned(user.getIsBanned());
        dto.setIsActive(user.getIsActive());
        dto.setIsSystem(user.getIsSystem());
    }

    public OverviewDto userToOverviewDto(User user) {
        if(!isLoaded(user) || user == null) {
            return null;
        }

        OverviewDto dto = new OverviewDto();
        mapBase(dto, user);

        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setBirthDate(dateToString(user.getBirthDate()));
        dto.setGender(user.getGender());
        dto.setLocation(user.getLocation());
        dto.setSite(user.getSite());
        dto.setSignature(user.getSignature());
        dto.setIsActivated(user.getIsActivated());
        dto.setHideEmail(user.getHideEmail());
        dto.setHideBirthdate(user.getHideBirthdate());

        return dto;
    }

    public GroupsDto userToGroupsDto(User user) {
        if(!isLoaded(user) || user == null) {
            return null;
        }

        GroupsDto dto = new GroupsDto();
        mapBase(dto, user);

        if(isLoaded(user.getGroups())) {
            dto.setGroups(groupToGroupDto(user.getGroups()));
        }

        return dto;
    }

    public List<GroupDto> groupToGroupDto(Collection<Group> groups) {
        List<GroupDto> ret = new ArrayList<>();
        if(!isLoaded(groups) || CollectionUtils.isEmpty(groups)) {
            return ret;
        }
        for(Group group : groups) {
            GroupDto groupDto = new GroupDto();
            groupDto.setId(String.valueOf(group.getId()));
            groupDto.setCode(group.getCode());
            groupDto.setName(group.getName());
            groupDto.setDescription(group.getDescription());
            ret.add(groupDto);
        }

        return ret;
    }

    public SecurityDto userToSecurityDto(User user) {
        if(!isLoaded(user) || user == null) {
            return null;
        }

        SecurityDto dto = new SecurityDto();
        mapBase(dto, user);

        dto.setSecretAnswer(user.getSecretAnswer());
        dto.setSecretQuestion(user.getSecretQuestion());

        return dto;
    }

    public StatisticsDto userToStatisticsDto(User user, long topicsCount) {
        if(!isLoaded(user) || user == null) {
            return null;
        }

        StatisticsDto dto = new StatisticsDto();
        mapBase(dto, user);

        dto.setRegisteredDate(dateTimeToString(user.getRegisteredDate()));
        dto.setLastLoginDate(dateTimeToString(user.getLastLoginDate()));
        dto.setPostsCount(user.getPostsCount());
        dto.setTopicsCount((int)topicsCount);
        dto.setGoodKarma(user.getGoodKarma());
        dto.setBadKarma(user.getBadKarma());
        dto.setTimeLoggedIn(user.getTimeLoggedIn());

        return dto;
    }

    public EditOverviewForm userToEditOverviewForm(User user) {
        if(!isLoaded(user) || user == null) {
            return null;
        }
        EditOverviewForm form = new EditOverviewForm();
        form.setName(user.getName());
        form.setSurname(user.getSurname());
        form.setGender(user.getGender());
        form.setBirthDate(user.getBirthDate());
        form.setLocation(user.getLocation());
        form.setSite(user.getSite());
        form.setHideEmail(user.getHideEmail());
        form.setHideBirthdate(user.getHideBirthdate());
        form.setShowOnline(user.getShowOnline());
        form.setSignature(user.getSignature());

        return form;
    }

    public void updateUserFromForm(User userEntity, EditOverviewForm form) {
        if(userEntity == null || form == null || !isLoaded(userEntity)) {
            return;
        }
        userEntity.setName(form.getName());
        userEntity.setSurname(form.getSurname());
        userEntity.setGender(form.getGender());
        userEntity.setBirthDate(form.getBirthDate());
        userEntity.setLocation(form.getLocation());
        userEntity.setSite(form.getSite());
        userEntity.setHideBirthdate(form.getHideBirthdate());
        userEntity.setHideEmail(form.getHideEmail());
        userEntity.setShowOnline(form.getShowOnline());
        userEntity.setSignature(form.getSignature());
    }
}
