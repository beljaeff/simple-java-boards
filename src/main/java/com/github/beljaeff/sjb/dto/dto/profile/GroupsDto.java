package com.github.beljaeff.sjb.dto.dto.profile;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupsDto extends ProfileDto {
    private List<GroupDto> groups;
}