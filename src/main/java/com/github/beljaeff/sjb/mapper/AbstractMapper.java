package com.github.beljaeff.sjb.mapper;

import org.springframework.beans.factory.annotation.Value;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnitUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

abstract public class AbstractMapper {
    @PersistenceContext
    private EntityManager entityManager;

    @Value("${date.pattern}")
    private String datePattern;

    @Value("${date.time.pattern}")
    private String dateTimePattern;

    protected String dateToString(LocalDate date) {
        return date == null ? null : date.format(DateTimeFormatter.ofPattern(datePattern));
    }

    protected String dateTimeToString(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(DateTimeFormatter.ofPattern(dateTimePattern));
    }

    protected boolean isLoaded(Object o) {
        PersistenceUnitUtil unitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
        return unitUtil.isLoaded(o);
    }
}