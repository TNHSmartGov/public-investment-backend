package com.tnh.baseware.core.dtos.investment.capital;

import java.util.UUID;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;

import org.springframework.hateoas.RepresentationModel;

import com.tnh.baseware.core.dtos.investment.progress.DisbursementDTO;
import com.tnh.baseware.core.entities.audit.Identifiable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tnh.baseware.core.dtos.investment.progress.AllocationExecutionDTO;

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
public class CapitalAllocationDetailDTO extends RepresentationModel<CapitalAllocationDetailDTO> implements Identifiable<UUID> {
    
    UUID id;
    Date allocationDate;
    BigDecimal amount;
    String description;
    Boolean deleted;

    List<DisbursementDTO> disbursements;
    List<AllocationExecutionDTO> allocationExecutions;
}
