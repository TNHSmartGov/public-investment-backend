package com.tnh.baseware.core.forms.investment.capital;

import java.math.BigDecimal;
import java.util.UUID;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
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

    @NotBlank(message = "{code.not.blank}")
    @Schema(description = "Mã quản lý của kế hoạch năm", example = "PL-2024-001")
    String code;

    @NotNull(message = "{amount.not.null}")
    @PositiveOrZero(message = "{amount.must.be.positive}")
    @Schema(description = "Số tiền phân bổ trong năm", example = "5000000000.00")
    BigDecimal amount;

    @NotNull(message = "{year.not.null}")
    @Min(value = 2000, message = "{year.invalid}")
    @Max(value = 2100, message = "{year.invalid}")
    @Schema(description = "Năm kế hoạch", example = "2024")
    Integer year;

    @Schema(description = "Mô tả chi tiết hoặc ghi chú")
    String description;

    @NotNull(message = "{capitalPlanId.not.null}")
    @Schema(description = "ID của Kế hoạch trung hạn (CapitalPlan) cha")
    UUID capitalPlanId;

    @NotNull(message = "{projectCapitalAllocationId.not.null}")
    @Schema(description = "ID phiếu phân bổ vốn trung hạn cho dự án")
    UUID projectCapitalAllocationId;
    
}
