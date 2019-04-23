package com.github.beljaeff.sjb.model;

import com.github.beljaeff.sjb.model.common.IdentifiedActiveEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.github.beljaeff.sjb.enums.EntityType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NamedNativeQuery(
    name = CommonEntity.PROC_GET_ANCESTORS,
    query = "SELECT * FROM get_entity_ancestors(:entityId, :entityType)",
    resultClass = CommonEntity.class
)

@NamedNativeQuery(
    name = CommonEntity.PROC_GET_BREADCRUMBS,
    query = "SELECT * FROM get_entity_ancestors(:entityId, :entityType) ORDER BY id DESC",
    resultClass = CommonEntity.class
)

@Getter
@Setter
@NoArgsConstructor
@Entity
public class CommonEntity extends IdentifiedActiveEntity {

    public static final String PROC_GET_ANCESTORS = "procedure.getAncestors";
    public static final String PROC_GET_BREADCRUMBS = "procedure.getBreadcrumbs";

    @Column(name = "id_entity")
    private Integer idEntity;

    @NotBlank
    @Size(min = 2, max = 128)
    @Column(nullable = false)
    private String title;

    @NotNull
    @Column(name = "type")
    private EntityType type;
}
