package com.tnh.baseware.core.dtos.investment.statistic;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectStatisticDTO {
    UUID id;
    String code;
    String name;
    BigDecimal totalInvestmentPlanned; // Tổng mức đầu tư
    BigDecimal yearPlanAmount;         // Kế hoạch vốn năm
    BigDecimal executionAmount;        // Lũy kế thực hiện đến tháng/năm
    BigDecimal disbursementAmount;     // Lũy kế giải ngân đến tháng/năm
}
