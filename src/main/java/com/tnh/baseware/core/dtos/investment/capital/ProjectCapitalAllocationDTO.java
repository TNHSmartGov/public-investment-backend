package com.tnh.baseware.core.dtos.investment.capital;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tnh.baseware.core.entities.audit.Identifiable;
import com.tnh.baseware.core.dtos.investment.project.ProjectDTO;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectCapitalAllocationDTO extends RepresentationModel<ProjectCapitalAllocationDTO> implements Identifiable<UUID> {

    UUID id;
    UUID projectId;      // Thêm trường này
    String projectName;  // Thêm trường này
    UUID capitalPlanId;  // Thêm trường này
    String capitalPlanName; // Thêm trường này

    ProjectDTO project;
    CapitalPlanDTO capitalPlan;

    BigDecimal amountInMediumTerm;
    List<CapitalPlanLineDTO> capitalPlanLines;
}
