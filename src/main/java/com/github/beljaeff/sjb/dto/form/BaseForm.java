package com.github.beljaeff.sjb.dto.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseForm {
    private String formUrl;
    private String saveUrl;
    private String cancelUrl;
    private String finishUrl;
}
