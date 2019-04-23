package com.github.beljaeff.sjb.dto.dto.page;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.github.beljaeff.sjb.dto.dto.BaseDto;
import com.github.beljaeff.sjb.dto.dto.BreadcrumbDto;
import com.github.beljaeff.sjb.dto.form.BaseForm;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FormPageDto <S extends BaseDto, T extends BaseForm> extends PageDto<S> {
    T form;

    public FormPageDto(S entity, T form, String title, List<BreadcrumbDto> breadcrumbs) {
        super(entity, title, breadcrumbs);
        this.form = form;
    }
}
