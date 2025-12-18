package com.tnh.baseware.core.forms.investment.progress;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.annotation.JsonFormat;

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
public class DisbursementEditorForm {

    @NotNull(message = "{investmentItem.not.null}")
    String investmentItem;

    String code;

    @NotNull(message = "{amount.not.null}")
    BigDecimal amount;

    @NotNull(message = "{execution.date.not.null}")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/yyyy")
    Date disbursementDate;

    @NotNull(message = "{responsible.person.not.null}")
    String responsiblePerson;

    Boolean isApproved;

    @NotNull(message = "{capital.allocation.detail.id.not.null}")
    @Schema(description = "Values are retrieved from 'CapitalAllocationDetails'")
    UUID capitalAllocationDetailId;

    @Schema(description = "Values are input when change information of amount, executionDate, responsiblePerson")
    UUID parentId;

    String description;

    Boolean deleted;

}
