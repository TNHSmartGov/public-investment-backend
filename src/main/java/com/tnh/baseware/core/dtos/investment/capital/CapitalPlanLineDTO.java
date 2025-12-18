package com.tnh.baseware.core.dtos.investment.capital;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tnh.baseware.core.entities.audit.Identifiable;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CapitalPlanLineDTO extends RepresentationModel<CapitalPlanLineDTO> implements Identifiable<UUID> {

    UUID id;
    String code;
    BigDecimal amount;
    CapitalPlanDTO capitalPlan;
    CapitalDTO capital;
    Date planLineDate;
    String description;
}
