package com.tnh.baseware.core.dtos.investment.project;

import com.tnh.baseware.core.dtos.audit.CategoryDTO;
import com.tnh.baseware.core.dtos.adu.OrganizationDTO;
import com.tnh.baseware.core.dtos.investment.ApprovalDecisionDTO;
import com.tnh.baseware.core.dtos.investment.IndustryDTO;
import com.tnh.baseware.core.dtos.investment.capital.CapitalAllocationDTO;
import com.tnh.baseware.core.dtos.investment.bid.BidPlanDTO;
import com.tnh.baseware.core.dtos.investment.bid.PackageBidDTO;
import com.tnh.baseware.core.dtos.investment.construction.ConstructionSiteDTO;
import com.tnh.baseware.core.dtos.investment.construction.LandClearanceDTO;
import com.tnh.baseware.core.entities.audit.Identifiable;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

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
public class ProjectDTO extends RepresentationModel<ProjectDTO> implements Identifiable<UUID> {

    UUID id;
    String code;
    String name;

    IndustryDTO industry;
    CategoryDTO group;
    CategoryDTO projectLevel;
    OrganizationDTO ownerOrg;
    OrganizationDTO pmuOrg;
    OrganizationDTO admOrg;
    List<CapitalAllocationDTO> capitalAllocations;
    List<ApprovalDecisionDTO> approvalDecisions;
    List<BidPlanDTO> bidPlans;
    List<PackageBidDTO> packageBids;
    List<ConstructionSiteDTO> constructionSites;
    List<LandClearanceDTO> landClearances;

    BigDecimal totalInvestmentPlanned;

    String status;
    String startPlanDate;
    String endPlanDate;
    String description;
    Boolean isApproved;
}
