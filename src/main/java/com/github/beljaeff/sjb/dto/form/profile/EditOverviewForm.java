package com.github.beljaeff.sjb.dto.form.profile;

import com.github.beljaeff.sjb.enums.Gender;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
public class EditOverviewForm {
    @NotBlank(message = "{edit.overview.form.name.empty}")
    @Size(min = 2, max = 128, message = "{edit.overview.form.name.incorrect.size}")
    private String name;

    @Size(max = 128, message = "{edit.overview.form.surname.too.long}")
    private String surname;

    private Gender gender;

    private LocalDate birthDate;

    @Size(max = 128, message = "{edit.overview.form.location.too.long}")
    private String location;

    @Size(max = 128, message = "{edit.overview.form.site.too.long}")
    private String site;

    private boolean hideEmail;

    private boolean hideBirthdate;

    private boolean showOnline;

    @Size(max = 32, message = "{edit.overview.form.signature.too.long}")
    private String signature;
}