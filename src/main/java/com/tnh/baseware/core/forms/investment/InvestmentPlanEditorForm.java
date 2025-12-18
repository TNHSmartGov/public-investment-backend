package com.tnh.baseware.core.forms.investment;

import java.time.LocalDate;

import com.tnh.baseware.core.entities.investment.Project;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class InvestmentPlanEditorForm {

    @NotNull(message = "{project.not.null}")
    @Schema(description = "Values are retrieved from 'projects'")
    Project projectId;
    
    String name;

    String description;

    Integer phaseOrder;

    LocalDate startDate;

    LocalDate endDate;

    Double estimatedBudget;

    Double actualBudget;

    @Schema(description = "e.g. pending, approved, rejected")
    String status;

    Boolean isApproved;

}
