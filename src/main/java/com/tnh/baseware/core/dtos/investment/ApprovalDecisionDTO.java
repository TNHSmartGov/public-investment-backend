package com.tnh.baseware.core.dtos.investment;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import com.tnh.baseware.core.entities.audit.Identifiable;
import com.tnh.baseware.core.dtos.investment.project.ProjectSummaryDTO;
import com.tnh.baseware.core.dtos.adu.OrganizationDTO;
import com.tnh.baseware.core.dtos.doc.FileDocumentDTO;

import com.fasterxml.jackson.annotation.JsonInclude;

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
public class ApprovalDecisionDTO extends RepresentationModel<ApprovalDecisionDTO> implements Identifiable<UUID> {

    UUID id;
    String code;
    String name;    
    String type;
    String numberNo;
    Date signDate;

    ProjectSummaryDTO project;
    OrganizationDTO authorityOrg;
    FileDocumentDTO fileDocument;
    
    String summary;    
    String description;

    Date startDate;
    Date endDate;

    BigDecimal totalRegistrationAmount;
    BigDecimal totalAppraisalAmount;
    BigDecimal totalApprovalAmount;
}
