package com.github.beljaeff.sjb.model.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@MappedSuperclass
public abstract class NamedEntity extends IdentifiedActiveEntity {
    @NotBlank
    @Size(min = 2, max = 128)
    @Column(nullable = false)
    private String name;

    //TODO:
    /**
     * When true - this entity should not be removed (service layer check)
     */
    @Column(name="is_system", nullable = false, insertable = false, updatable = false)
    private boolean isSystem;
}