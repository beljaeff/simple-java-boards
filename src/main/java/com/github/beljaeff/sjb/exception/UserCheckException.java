package com.github.beljaeff.sjb.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.github.beljaeff.sjb.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserCheckException extends Exception {

    private boolean isNull;
    private boolean isActivated;
    private boolean isActive;
    private boolean isBanned;
    private boolean isBefore;
    private boolean isAfter;

    public UserCheckException(User user) {
        this.isNull = user == null;
        if(user != null) {
            this.isActivated = user.getIsActivated();
            this.isActive = user.getIsActive();
            this.isBanned = user.getIsBanned();
            this.isBefore = LocalDateTime.now().minusHours(24).isBefore(user.getLastValidationRequestDate());
            this.isAfter = LocalDateTime.now().minusHours(24).isAfter(user.getLastValidationRequestDate());
        }
    }
}