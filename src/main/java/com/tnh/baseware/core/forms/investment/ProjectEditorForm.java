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
public class ProjectEditorForm {

    @NotNull(message = "{code.not.null}")
    String code;

    @NotNull(message = "{name.not.null}")
    String name;

    @Schema(description = "Total planned investment for the project'")
    BigDecimal totalInvestmentPlanned;

    @NotNull(message = "{status.not.null}")
    @Schema(description = "1: Đang chuẩn bị, 2: Đang thực hiện, 3: Đã hoàn thành, 4: Đã hủy")
    String status;

    @Schema(description = "Start date of the project in format yyyyy")
    String startPlanDate;

    @Schema(description = "End date of the project in format yyyyy")
    String endPlanDate;

    String description;

    @NotNull(message = "{industry.not.null}")
    @Schema(description = "Values are retrieved from 'industries'")
    UUID industryId;

    @Schema(description = "Values are retrieved from 'categories'")
    UUID groupId;

    @Schema(description = "Values are retrieved from 'categories'")
    UUID projectLevelId;

    @NotNull(message = "{ownerOrg.not.null}")
    @Schema(description = "Values are retrieved from 'organizations'")
    UUID ownerOrgId;

    @Schema(description = "Values are retrieved from 'organizations'")
    UUID pmuOrgId;

    @Schema(description = "Values are retrieved from 'organizations'")
    UUID admOrgId;

}
