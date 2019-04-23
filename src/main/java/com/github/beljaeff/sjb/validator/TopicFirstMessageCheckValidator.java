package com.github.beljaeff.sjb.validator;

import com.github.beljaeff.sjb.dto.form.conversation.TopicForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.springframework.util.StringUtils.isEmpty;

public class TopicFirstMessageCheckValidator implements ConstraintValidator<TopicFirstMessageCheck, TopicForm> {

    private int maxSize;

    @Override
    public void initialize(TopicFirstMessageCheck constraintAnnotation) {
        this.maxSize = constraintAnnotation.maxSize();
    }

    @Override
    public boolean isValid(TopicForm form, ConstraintValidatorContext context) {
        if(form == null) {
            return false;
        }
        // Check message only for creating topic. When edit topic there is no message.
        return form.getId() != 0 || (!isEmpty(form.getMessage()) && form.getMessage().length() <= maxSize);
    }
}