package com.github.beljaeff.sjb.service;

public interface EmailService {

    void createAndSendActivationMessage(String email, String validationCode, String basePath);

    void createAndSendResetPasswordMessage(String email, String validationCode, String basePath);

    void createAndSendUserRegisteredMessage(String email, String basePath);

    void createAndSendResetPasswordSuccessMessage(String email, String basePath);
}