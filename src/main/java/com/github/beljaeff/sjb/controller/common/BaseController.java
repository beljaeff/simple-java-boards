package com.github.beljaeff.sjb.controller.common;

import com.github.beljaeff.sjb.dto.dto.BreadcrumbDto;
import com.github.beljaeff.sjb.service.RecordService;
import com.github.beljaeff.sjb.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

abstract public class BaseController {

    protected static final String ATTR_PAGE = "page";
    protected static final String ATTR_FORM = "form";
    protected static final String ATTR_ID = "id";
    protected static final String ATTR_BINDING_RESULT = "org.springframework.validation.BindingResult.form";
    protected static final String ATTR_BREADCRUMBS = "breadcrumbs";
    protected static final String ATTR_ACTIVE_LIST = "activeList";

    protected RecordService recordService;

    @Autowired
    public void setRecordService(RecordService recordService) {
        this.recordService = recordService;
    }

    public List<BreadcrumbDto> getBreadcrumbs() {
        String title = recordService.getText("home.text.header");
        String link = HttpUtils.getRootPath();

        List<BreadcrumbDto> ret = new ArrayList<>();
        ret.add(new BreadcrumbDto(link, title));
        return ret;
    }

    protected String buildSegmentsPath(String path, Object... segments) {
        return UriComponentsBuilder.newInstance().path(path).buildAndExpand(segments).toUriString();
    }

    protected String buildSegmentsQueryPath(String path, String query, Map<String, ?> uriVariables) {
        return UriComponentsBuilder.newInstance().path(path).query(query).buildAndExpand(uriVariables).toUriString();
    }
}