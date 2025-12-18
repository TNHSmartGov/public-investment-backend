package com.tnh.baseware.core.dtos.investment.project;

import com.tnh.baseware.core.dtos.audit.CategoryDTO;
import com.tnh.baseware.core.dtos.adu.OrganizationDTO;
import com.tnh.baseware.core.entities.audit.Identifiable;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ProjectCheckAprroveDTO extends RepresentationModel<ProjectDTO> implements Identifiable<UUID> {

    UUID id;
    String name;
    OrganizationDTO ownerOrg;
    OrganizationDTO pmuOrg;
    OrganizationDTO admOrg;
    CategoryDTO projectLevel;

    BigDecimal totalInvestmentPlanned;

    String startPlanDate;
    String endPlanDate;
    String description;
    Boolean isApproved;
}
