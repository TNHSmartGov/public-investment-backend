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
public class PackageBidEditorForm {

    @NotNull(message = "{name.not.null}")
    String name;

    @NotNull(message = "{value.not.null}")
    BigDecimal packageValue;
    
    @NotNull(message = "{bidding.time.not.null}")
    Date biddingTime;

    @NotNull(message = "{project.id.not.null}")
    @Schema(description = "Values are retrieved from 'projects'")
    UUID projectId;

    @NotNull(message = "{numberNo.not.null}")
    @Schema(description = "Values are retrieved from 'categories'")
    UUID biddingMethodId;

    @NotNull(message = "{numberNo.not.null}")
    @Schema(description = "Values are retrieved from 'categories'")
    UUID selectionProcedureId;

    @NotNull(message = "{contract.type.id.not.null}")
    @Schema(description = "Values are retrieved from 'categories'")
    UUID contractTypeId;

    Integer executionTime;
    String executionTimeUnit;

    @NotNull(message = "{bidding.field.id.not.null}")
    @Schema(description = "Values are retrieved from 'categories'")
    UUID biddingFieldId;

    @NotNull(message = "{bidding.form.id.not.null}")
    @Schema(description = "Values are retrieved from 'categories'")
    UUID biddingFormId;

    String description;
    
}
