package com.github.beljaeff.sjb.exception;

import lombok.Getter;
import lombok.Setter;
import com.github.beljaeff.sjb.model.User;

@Getter
@Setter
public class CredentialsNotUniqueException extends Exception {
    private String email;
    private String nickName;

    public CredentialsNotUniqueException(User user, String originalEmail, String originalNickName) {
        super();
        // Decide email or nickname not unique or both
        if(user.getEmail().equals(originalEmail.toLowerCase())) {
            email = user.getEmail();
        }
        if(user.getNickName().equals(originalNickName)) {
            nickName = user.getNickName();
        }
    }
}