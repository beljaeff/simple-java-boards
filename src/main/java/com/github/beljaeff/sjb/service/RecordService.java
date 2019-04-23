package com.github.beljaeff.sjb.service;

public interface RecordService {
    String getText(String code);

    String getText(String code, String...args);
}