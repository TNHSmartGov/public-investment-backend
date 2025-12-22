package com.tnh.baseware.core.dtos.investment.history;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tnh.baseware.core.entities.audit.Identifiable;

import org.springframework.hateoas.RepresentationModel;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class DisbursementHistoryDTO extends RepresentationModel<DisbursementHistoryDTO> implements Identifiable<UUID> {

    UUID id;
    UUID originalId;
    String actionType;

    String investmentItem;
    String code;
    BigDecimal amount;
    Instant disbursementDate;
    String responsiblePerson;
    String voucherNumber;
    Boolean isApproved;
    String disbursementType;
    String description;

    UUID projectId;
    UUID planLineId;
}
