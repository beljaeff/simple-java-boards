package com.github.beljaeff.sjb.dto.dto.profile;

import com.github.beljaeff.sjb.dto.dto.attachment.AttachmentDto;
import com.github.beljaeff.sjb.enums.ProfileSection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
abstract public class ProfileDto {
    private int id;
    private String nickName;
    private AttachmentDto avatar;
    private ProfileSection profileSection;
    private boolean isBanned;
    private boolean isActive;
    private boolean isSystem;
}
