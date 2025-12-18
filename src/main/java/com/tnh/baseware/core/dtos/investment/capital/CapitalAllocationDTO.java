package com.tnh.baseware.core.dtos.investment.capital;

import com.tnh.baseware.core.dtos.investment.project.ProjectSummaryDTO;
import com.tnh.baseware.core.entities.audit.Identifiable;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.List;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CapitalAllocationDTO extends RepresentationModel<CapitalAllocationDTO> implements Identifiable<UUID> {

    UUID id;
    Boolean eligible;
    String note;
    BigDecimal amount;

    CapitalPlanDTO capitalPlan;
    CapitalDTO capital;
    ProjectSummaryDTO project;

    List<CapitalAllocationDetailDTO> capitalAllocationDetails;

    String description;
    
}
