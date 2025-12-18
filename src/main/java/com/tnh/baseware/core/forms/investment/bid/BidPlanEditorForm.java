package com.tnh.baseware.core.forms.investment.bid;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BidPlanEditorForm {

    @NotNull(message = "{numberNo.not.null}")
    String numberNo;

    @NotNull(message = "{signDate.not.null}")
    Date signDate;

    @NotNull(message = "{projects.not.null}")
    @Schema(description = "Values are retrieved from 'projects'")
    UUID projectId;

    @NotNull(message = "{organizations.not.null}")
    @Schema(description = "Values are retrieved from 'organizations'")
    UUID authorityOrgId;

    @NotNull(message = "{file.documents.not.null}")
    @Schema(description = "Values are retrieved from 'file-documents'")
    UUID fileDocumentId;

    BigDecimal totalProjectAmount;
    BigDecimal totalImplementedAmount;
    BigDecimal totalBidPackageAmount;

    String description;
    
}
