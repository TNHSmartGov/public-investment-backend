package com.tnh.baseware.core.forms.investment.capital;

import java.math.BigDecimal;
import java.util.UUID;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProjectCapitalAllocationEditorForm {

    @NotNull(message = "{project.id.not.null}")
    private UUID projectId;

    @NotNull(message = "{capitalPlan.id.not.null}")
    private UUID capitalPlanId;

    @NotNull(message = "{amount.not.null}")
    @Positive(message = "{amount.must.be.positive}")
    @Schema(description = "Tổng mức vốn trung hạn cấp cho dự án từ nguồn này")
    private BigDecimal amountInMediumTerm;
}
