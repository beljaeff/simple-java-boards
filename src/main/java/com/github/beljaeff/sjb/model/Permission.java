package com.github.beljaeff.sjb.model;

import com.github.beljaeff.sjb.model.common.NamedEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "permissions")
public class Permission extends NamedEntity {

    @NotBlank
    @Size(min = 2, max = 32)
    private String code;

    @Size(max = 128)
    private String description;
}