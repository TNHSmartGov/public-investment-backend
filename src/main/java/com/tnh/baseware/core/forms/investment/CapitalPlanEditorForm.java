package com.tnh.baseware.core.forms.investment;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotBlank;
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
public class CapitalPlanEditorForm {

    @NotNull(message = "{code.not.null}")
    String code;

    @NotBlank(message = "{name.not.blank}")
    String name;

    @NotBlank(message = "{startYear.not.blank}")
    String startYear;

    @NotBlank(message = "{endYear.not.blank}")
    String endYear;

    @NotNull(message = "{totalAmountPlan.not.null}")
    Long totalAmountPlan;

    @NotNull(message = "{typePlan.not.null}")
    String typePlan;

    @NotNull(message = "{isApproved.not.null}")
    Boolean isApproved;

    String description;
}
