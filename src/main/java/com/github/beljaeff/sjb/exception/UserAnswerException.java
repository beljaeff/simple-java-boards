package com.github.beljaeff.sjb.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAnswerException extends Exception {
    private boolean isAnswerCorrect;
    private int answerTries;
    private String secretQuestion;
    private int answerTriesLeft;

    public UserAnswerException(boolean isAnswerCorrect, int answerTries, String secretQuestion, int answerTriesLeft) {
        this.isAnswerCorrect = isAnswerCorrect;
        this.answerTries = answerTries;
        this.secretQuestion = secretQuestion;
        this.answerTriesLeft = answerTriesLeft;
    }
}
