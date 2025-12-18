package com.tnh.baseware.core.forms.investment;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.Date;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import com.fasterxml.jackson.annotation.JsonFormat;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CapitalAllocationDetailEditorForm {

    @NotNull(message = "{capitalallocationid.not.null}")
    @Schema(description = "Values are retrieved from 'capitalAllocations'")
    UUID capitalAllocationId;

    BigDecimal amount;

    @NotNull(message = "{allocationdate.not.null}")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/yyyy")
    Date allocationDate;

    String description;    

}
