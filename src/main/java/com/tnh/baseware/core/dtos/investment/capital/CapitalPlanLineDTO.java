package com.tnh.baseware.core.dtos.investment.capital;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tnh.baseware.core.entities.audit.Identifiable;

import lombok.*;
import lombok.experimental.FieldDefaults;

import org.springframework.hateoas.RepresentationModel;

import com.tnh.baseware.core.dtos.investment.progress.AllocationExecutionDTO;
import com.tnh.baseware.core.dtos.investment.progress.DisbursementDTO;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CapitalPlanLineDTO extends RepresentationModel<CapitalPlanLineDTO> implements Identifiable<UUID> {

    UUID id;
    String code;
    BigDecimal amount;
    Integer year;
    String description;

    UUID capitalPlanId;

    CapitalPlanDTO capitalPlan;
    List<DisbursementDTO> disbursements;
    List<AllocationExecutionDTO> allocationExecutions;

    // Các trường tính toán hiển thị
    BigDecimal totalExecution;
    BigDecimal totalDisbursed;
    BigDecimal remainingAmount;
}
