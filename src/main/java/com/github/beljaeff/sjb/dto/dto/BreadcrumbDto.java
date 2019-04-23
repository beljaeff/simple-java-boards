package com.github.beljaeff.sjb.dto.dto;

import com.github.beljaeff.sjb.model.CommonEntity;
import com.github.beljaeff.sjb.util.HttpUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static org.springframework.util.StringUtils.isEmpty;

@Getter
@Setter
@NoArgsConstructor
public class BreadcrumbDto {
    private String link;
    private String title;
    private Boolean isActive;

    public BreadcrumbDto(String link, String title) {
        this.link = link;
        this.title = title;
    }

    public BreadcrumbDto(CommonEntity entity) {
        if(entity == null) {
            throw new IllegalArgumentException("breadcrumb is null");
        }
        createInstance(entity.getIdEntity(), entity.getType().getType(), entity.getTitle(), entity.getIsActive());
    }

    public BreadcrumbDto(int idEntity, String type, String title, Boolean isActive, String...suffixes) {
        if(idEntity < 0 || isEmpty(type) || isEmpty(title)) {
            throw new IllegalArgumentException("can not create breadcrumb");
        }
        createInstance(idEntity, type, title, isActive, suffixes);
    }

    private void createInstance(int idEntity, String type, String title, Boolean isActive, String...suffixes) {
        this.title = title;
        this.isActive = isActive;
        this.link = HttpUtils.makeLink(type, idEntity, suffixes);
    }
}