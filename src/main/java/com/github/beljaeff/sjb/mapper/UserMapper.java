package com.github.beljaeff.sjb.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import com.github.beljaeff.sjb.dto.dto.UserDto;
import com.github.beljaeff.sjb.dto.dto.pagination.PaginatedDto;
import com.github.beljaeff.sjb.enums.EntityType;
import com.github.beljaeff.sjb.enums.ProfileSection;
import com.github.beljaeff.sjb.model.Group;
import com.github.beljaeff.sjb.model.PagedEntityList;
import com.github.beljaeff.sjb.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.github.beljaeff.sjb.util.HttpUtils.makeLink;

@Component
public class UserMapper extends AbstractMapper {

    private AttachmentMapper attachmentMapper;

    @Autowired
    public void setAttachmentMapper(AttachmentMapper attachmentMapper) {
        this.attachmentMapper = attachmentMapper;
    }

    public UserDto userToUserDto(User author) {
        if(!isLoaded(author) || author == null) {
            return null;
        }
        UserDto dto = new UserDto();
        dto.setId(author.getId());
        dto.setNickName(author.getNickName());
        dto.setIsActive(author.getIsActive());
        dto.setIsActivated(author.getIsActivated());
        dto.setIsBanned(author.getIsBanned());
        dto.setPostsCount(author.getPostsCount());
        dto.setGoodKarma(author.getGoodKarma());
        dto.setBadKarma(author.getBadKarma());
        dto.setLocation(author.getLocation());
        dto.setSignature(author.getSignature());
        if(isLoaded(author.getAvatar())) {
            dto.setAvatar(attachmentMapper.attachmentToAttachmentDto(author.getAvatar()));
        }
        if(isLoaded(author.getGroups())) {
            Group group = findWithMinWeight(author.getGroups());
            if(group != null) {
                dto.setGroupColor(group.getColor());
                dto.setGroupName(group.getName());
            }
        }
        dto.setLinkToProfile(author.getId() >= 0 ? makeLink(EntityType.PROFILE.getType(), author.getId(), ProfileSection.OVERVIEW.getCode()) : "");

        return dto;
    }

    public PaginatedDto<UserDto> userToUserDto(List<User> users) {
        if(!isLoaded(users)) {
            return null;
        }
        List<UserDto> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(users)) {
            for(User user : users) {
                UserDto dto = userToUserDto(user);
                if(dto != null) {
                    list.add(dto);
                }
            }
        }

        return new PaginatedDto<>(list.size(), 1, 1, list.size(), list);
    }

    public PaginatedDto<UserDto> userToUserDto(PagedEntityList<User> users) {
        if(users == null) {
            return new PaginatedDto<>();
        }
        PaginatedDto<UserDto> result = userToUserDto(users.getList());
        result.setCurrentPage(users.getCurrentPage());
        result.setPageSize(users.getPageSize());
        result.setTotal(users.getTotal());
        result.setTotalPages(users.getTotalPages());

        return result;
    }

    private static Group findWithMinWeight(Set<Group> groups) {
        if(CollectionUtils.isEmpty(groups)) {
            return null;
        }
        Group ret = null;
        int min = Integer.MAX_VALUE;
        for(Group group : groups) {
            if(group != null && group.getWeight() < min) {
                min = group.getWeight();
                ret = group;
            }
        }

        return ret;
    }
}
