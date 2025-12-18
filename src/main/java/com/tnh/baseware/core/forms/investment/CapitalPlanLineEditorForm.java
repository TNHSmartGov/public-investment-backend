package com.tnh.baseware.core.forms.investment;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class CapitalPlanLineEditorForm {

    BigDecimal amount;
    String code;
    String description;
    
    @NotNull(message = "{capitalplanid.not.null}")
    @Schema(description = "Values are retrieved from 'capitalplans'")
    UUID capitalPlanId;

    @NotNull(message = "{capitalid.not.null}")
    @Schema(description = "Values are retrieved from 'capitals'")
    UUID capitalId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy", timezone = "UTC")
    Date planLineDate;

}
