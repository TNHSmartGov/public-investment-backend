package com.tnh.baseware.core.dtos.investment.capital;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tnh.baseware.core.entities.audit.Identifiable;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CapitalPlanDTO extends RepresentationModel<CapitalPlanDTO> implements Identifiable<UUID> {

    UUID id;
    String code;
    String name;
    Integer startYear;
    Integer endYear;
    Long totalAmountPlan;
    Boolean isApproved;
    String typePlan;
    String description;

}
