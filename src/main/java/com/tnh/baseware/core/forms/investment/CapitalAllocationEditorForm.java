package com.tnh.baseware.core.forms.investment;

import java.math.BigDecimal;
import java.util.UUID;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CapitalAllocationEditorForm {

    @NotNull(message = "{eligible.not.null}")
    Boolean eligible;
    
    String note;

    @NotNull(message = "{amount.not.null}")
    BigDecimal amount;

    String description;

    @NotNull(message = "{capitalplanid.not.null}")
    @Schema(description = "Values are retrieved from 'capitalplans'")
    UUID capitalPlanLineId;
  
    @NotNull(message = "{capitalid.not.null}")
    @Schema(description = "Values are retrieved from 'capitals'")
    UUID capitalId;

    @NotNull(message = "{projectid.not.null}")
    @Schema(description = "Values are retrieved from 'projects'")
    UUID projectId;

}
