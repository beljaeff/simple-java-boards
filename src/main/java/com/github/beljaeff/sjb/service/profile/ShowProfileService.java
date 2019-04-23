package com.github.beljaeff.sjb.service.profile;

import com.github.beljaeff.sjb.dto.dto.profile.ProfileDto;

public interface ShowProfileService {
    ProfileDto getOverview(int userId);
    ProfileDto getSecurity(int userId);
    ProfileDto getGroups(int userId);
    ProfileDto getStatistics(int userId);
}