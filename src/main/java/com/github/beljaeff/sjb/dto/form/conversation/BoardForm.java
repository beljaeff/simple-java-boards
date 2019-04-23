package com.github.beljaeff.sjb.dto.form.conversation;

import com.github.beljaeff.sjb.dto.form.BaseForm;
import com.github.beljaeff.sjb.validator.BoardToCategoryPermissionCheck;
import com.github.beljaeff.sjb.validator.CategoryForBoardCheck;
import com.github.beljaeff.sjb.validator.PositionCheck;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
@Setter
@CategoryForBoardCheck(message = "{edit.board.category.parent.set.both}")
@BoardToCategoryPermissionCheck
public class BoardForm extends BaseForm {
    @PositiveOrZero(message = "{board.form.id.incorrect}")
    private int id;

    private Integer parentBoard;

    private Integer category;

    @NotBlank(message = "{board.form.title.empty}")
    @Size(min = 2, max = 128, message = "{board.form.title.size.incorrect}")
    private String title;

    @Size(max = 512, message = "{board.form.description.size.incorrect}")
    private String description;

    private Boolean isActive;

    //TODO: implement dictionary and validator
    @NotBlank(message = "{board.form.icon.empty}")
    private String icon = "fas fa-comments";

    @PositionCheck(message = "{category.form.position.incorrect}")
    private String position;
}