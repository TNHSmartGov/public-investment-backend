package com.tnh.baseware.core.dtos.investment.progress;

import java.util.UUID;
import java.math.BigDecimal;
import java.util.Date;

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
public class AllocationExecutionDTO extends RepresentationModel<AllocationExecutionDTO> implements Identifiable<UUID> {

    UUID id;
    String investmentItem;
    String code;
    BigDecimal amount;
    Date executionDate;
    String responsiblePerson;
    Boolean isApproved;
    Boolean deleted;
    String description;

}
