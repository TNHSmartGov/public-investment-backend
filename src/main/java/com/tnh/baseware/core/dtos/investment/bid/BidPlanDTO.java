package com.tnh.baseware.core.dtos.investment.bid;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tnh.baseware.core.entities.audit.Identifiable;
import com.tnh.baseware.core.dtos.investment.project.ProjectSummaryDTO;
import com.tnh.baseware.core.dtos.adu.OrganizationDTO;
import com.tnh.baseware.core.dtos.doc.FileDocumentDTO;

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
public class BidPlanDTO extends RepresentationModel<BidPlanDTO> implements Identifiable<UUID> {

    UUID id;
    String numberNo;
    Date signDate;

    ProjectSummaryDTO project;
    OrganizationDTO authorityOrg;
    FileDocumentDTO fileDocument;
    
    BigDecimal totalProjectAmount;
    BigDecimal totalImplementedAmount;
    BigDecimal totalBidPackageAmount;

}
