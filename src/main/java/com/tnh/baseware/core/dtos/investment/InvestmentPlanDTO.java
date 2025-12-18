package com.tnh.baseware.core.dtos.investment;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import com.tnh.baseware.core.entities.audit.Identifiable;
import com.tnh.baseware.core.entities.investment.Project;

import lombok.*;
import lombok.experimental.FieldDefaults;

import com.fasterxml.jackson.annotation.JsonInclude;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvestmentPlanDTO extends RepresentationModel<InvestmentPlanDTO> implements Identifiable<UUID> {

    UUID id;
    Project projectId;
    String name;
    String description;
    Integer phaseOrder;
    LocalDate startDate;
    LocalDate endDate;
    Double estimatedBudget;
    Double actualBudget;
    String status;
    Boolean isApproved;
    
}
