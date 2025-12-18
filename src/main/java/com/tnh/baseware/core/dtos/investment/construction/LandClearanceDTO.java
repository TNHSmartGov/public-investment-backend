package com.tnh.baseware.core.dtos.investment.construction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tnh.baseware.core.dtos.doc.FileDocumentDTO;
import com.tnh.baseware.core.dtos.investment.project.ProjectSummaryDTO;
import com.tnh.baseware.core.entities.audit.Identifiable;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LandClearanceDTO extends RepresentationModel<LandClearanceDTO> implements Identifiable<UUID> {

    UUID id;
    Date implementationDate;

    ProjectSummaryDTO project;
    FileDocumentDTO fileDocument;

    String summary;
    
    BigDecimal implementationAmount;

}
