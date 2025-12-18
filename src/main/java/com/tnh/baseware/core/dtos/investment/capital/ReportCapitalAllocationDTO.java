package com.tnh.baseware.core.dtos.investment.capital;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;
import java.util.List;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportCapitalAllocationDTO {
    
    UUID id;
    BigDecimal amount;
    //List<CADSummaryDTO> capitalAllocationDetails;
    List<ReportCapitalAllocationDetailDTO> capitalAllocationDetails;

}
