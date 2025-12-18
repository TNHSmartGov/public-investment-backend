package com.tnh.baseware.core.dtos.investment.capital;

import java.util.UUID;
import java.util.Date;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CADSummaryDTO {
    
    UUID id;
    Date allocationDate;
    BigDecimal amount;

}
