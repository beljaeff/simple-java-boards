package com.github.beljaeff.sjb.model.common;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.github.beljaeff.sjb.model.Board;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.SequenceGenerator;
import javax.persistence.StoredProcedureParameter;

@NamedStoredProcedureQuery(
    name = Board.PROC_IS_ENTITY_ACTIVE,
    procedureName = "IS_ENTITY_ACTIVE",
    parameters = {
        @StoredProcedureParameter(
            name = "entity_id",
            type = Integer.class,
            mode = ParameterMode.IN),
        @StoredProcedureParameter(
            name = "entity_type",
            type = String.class,
            mode = ParameterMode.IN),
        @StoredProcedureParameter(
            name = "check_parents",
            type = Boolean.class,
            mode = ParameterMode.IN)
    }
)

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public abstract class IdentifiedActiveEntity {
    public static final String PROC_IS_ENTITY_ACTIVE = "procedure.isEntityActive";

    public static final int START_POSITION = 100;

    @Id
    @SequenceGenerator(name = "sjb_id_seq", sequenceName = "sjb_id_seq", allocationSize = 1, initialValue = START_POSITION)
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sjb_id_seq")
    private int id;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;
}
