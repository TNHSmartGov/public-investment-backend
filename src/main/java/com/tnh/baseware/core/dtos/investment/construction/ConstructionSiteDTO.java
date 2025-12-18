package com.tnh.baseware.core.dtos.investment.construction;

import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tnh.baseware.core.dtos.adu.CommuneDTO;
import com.tnh.baseware.core.dtos.adu.ProvinceDTO;
import com.tnh.baseware.core.dtos.investment.project.ProjectSummaryDTO;
import com.tnh.baseware.core.entities.audit.Identifiable;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConstructionSiteDTO extends RepresentationModel<ConstructionSiteDTO> implements Identifiable<UUID> {

    UUID id;
    ProvinceDTO province;
    CommuneDTO commune;
    ProjectSummaryDTO project;

}
