package com.tnh.baseware.core.dtos.investment.project;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tnh.baseware.core.entities.audit.Identifiable;

import java.math.BigDecimal;
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
    private UUID id;
    private String code;
    private String name;
    
    // Thông tin phân cấp
    private UUID parentProjectId;
    private String parentProjectName;
    private Boolean isProgram;

    // Thông tin định danh danh mục (Flat data)
    private String industryName;
    private String groupName;
    private String projectLevelName;
    
    // Thông tin tổ chức
    private String ownerOrgName;
    private String pmuOrgName;
    private String admOrgName;

    private BigDecimal totalInvestmentPlanned;
    private String status;
    private String startPlanDate;
    private String endPlanDate;
    private String description;
    private Boolean isApproved;
    
    // Các tập hợp con (Thường trả về số lượng hoặc danh sách tóm tắt)
    private Integer approvalDecisionCount;
    private Integer bidPlanCount;
}