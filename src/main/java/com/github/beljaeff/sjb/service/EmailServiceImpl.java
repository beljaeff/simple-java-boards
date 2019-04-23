package com.github.beljaeff.sjb.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final RecordService recordService;

    @Value("${mail.default.email.from}")
    private String defaultEmailFrom;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender, RecordService recordService) {
        this.mailSender = mailSender;
        this.recordService = recordService;
    }

    private void sendFromTemplate(String email, String messageTitle, String messageBody, String...bodyArgs) {
        String subject = recordService.getText(messageTitle);
        String body = recordService.getText(messageBody, bodyArgs);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom(defaultEmailFrom);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(body, true);
        }
        catch (MessagingException e) {
            log.warn("Can not create email message from '{}' to '{}' with subject '{}'", defaultEmailFrom, email, subject);
            log.warn(e.getMessage(), e);
        }
        send(message);
    }

    @Override
    @Async
    public void createAndSendActivationMessage(String email, String validationCode, String basePath) {
        Assert.notNull(email, "email should be set");
        Assert.notNull(validationCode, "validation code should be set");
        sendFromTemplate(email,
                         "sign.up.email.message.activation.title",
                         "sign.up.email.message.activation.body",
                         basePath, validationCode);
    }

    @Override
    @Async
    public void createAndSendResetPasswordMessage(String email, String validationCode, String basePath) {
        Assert.notNull(email, "email should be set");
        Assert.notNull(validationCode, "validation code should be set");
        sendFromTemplate(email,
                         "reset.password.email.message.activation.title",
                         "reset.password.email.message.activation.body",
                         basePath, validationCode);
    }

    @Override
    @Async
    public void createAndSendResetPasswordSuccessMessage(String email, String basePath) {
        Assert.notNull(email, "email should be set");
        sendFromTemplate(email,
                         "reset.password.email.message.success.title",
                         "reset.password.email.message.success.body",
                         basePath);
    }

    @Override
    public void createAndSendUserRegisteredMessage(String email, String basePath) {
        Assert.notNull(email, "email should be set");
        sendFromTemplate(email,
                         "sign.up.successful.title",
                         "sign.up.successful.body",
                         basePath);
    }

    private void send(MimeMessage message) {
        Assert.notNull(message, "message should be set");
        try {
            this.mailSender.send(message);
        }
        catch (MailException me) {
            log.warn("Error sending email message {}", message);
            log.warn(me.getMessage(), me);
        }
    }
}
