package com.tnh.baseware.core.dtos.investment.progress;


import java.math.BigDecimal;
import java.util.UUID;
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
public class DisbursementDTO extends RepresentationModel<DisbursementDTO> implements Identifiable<UUID> {

    UUID id;
    String investmentItem;
    String code;
    BigDecimal amount;
    Date disbursementDate;
    String responsiblePerson;
    Boolean isApproved;
    Boolean deleted;
    String description;

}
