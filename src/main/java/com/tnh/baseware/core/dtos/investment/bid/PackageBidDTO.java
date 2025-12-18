package com.tnh.baseware.core.dtos.investment.bid;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tnh.baseware.core.entities.audit.Category;
import com.tnh.baseware.core.entities.audit.Identifiable;
import com.tnh.baseware.core.dtos.investment.project.ProjectSummaryDTO;

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
public class PackageBidDTO extends RepresentationModel<PackageBidDTO> implements Identifiable<UUID> {

    UUID id;
    String name;

    BigDecimal packageValue;
    Date biddingTime;

    ProjectSummaryDTO project;

    Category biddingMethod;
    Category selectionProcedure;
    Category contractType;

    Integer executionTime;
    String executionTimeUnit;

    Category biddingField;
    Category biddingForm;

    String description;
    
}
