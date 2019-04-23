package com.github.beljaeff.sjb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

@Service
public class RecordServiceImpl implements RecordService {

    private final MessageSource messageSource;

    @Autowired
    public RecordServiceImpl(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public String getText(String code) {
        return messageSource.getMessage(code, null, getLocale());
    }

    @Override
    public String getText(String code, String...args) {
        return messageSource.getMessage(code, args, getLocale());
    }
}