package com.tnh.baseware.core.entities.investment;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tnh.baseware.core.entities.adu.Organization;
import com.tnh.baseware.core.entities.audit.Auditable;
import com.tnh.baseware.core.entities.audit.Category;
import com.tnh.baseware.core.entities.investment.bid.BidPlan;
import com.tnh.baseware.core.entities.investment.bid.PackageBid;
import com.tnh.baseware.core.entities.investment.capital.ProjectCapitalAllocation;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.tnh.baseware.core.entities.investment.construction.ConstructionSite;
import com.tnh.baseware.core.entities.investment.construction.LandClearance;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Project extends Auditable<String> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false)
    String code;

    @Column(nullable = false)
    String name;

    // Cấu trúc phân cấp
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Project parentProject; // Nếu null là dự án cấp tỉnh hoặc gói vốn tổng

    @Column(name = "is_program")
    private Boolean isProgram = false; // TRUE: Gói vốn (NTM), FALSE: Dự án cụ thể

    /*
    Dự án Cấp Tỉnh	isProgram = false, parent_id = null. Giao vốn trực tiếp vào dự án này.
    Gói vốn NTM	isProgram = true, parent_id = null. Giao vốn tổng cho Xã.
    Dự án Cấp Xã	isProgram = false, parent_id = [Gói vốn NTM]. Xã tự lập dự án con.
    */
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "industry_id", nullable = false)
    Industry industry; // Ngành

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    Category group; // Nhóm dự án

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_level_id", nullable = true)
    Category projectLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_org_id", nullable = false)
    Organization ownerOrg; // Chủ đầu tư

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pmu_org_id", nullable = false)
    Organization pmuOrg; // Quản lý dự án

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adm_org_id", nullable = false)
    Organization admOrg; // Địa bàn chính

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    Set<ApprovalDecision> approvalDecisions = new HashSet<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    Set<BidPlan> bidPlans = new HashSet<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    Set<PackageBid> packageBids = new HashSet<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    Set<ConstructionSite> constructionSites = new HashSet<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    Set<LandClearance> landClearances = new HashSet<>();

    BigDecimal totalInvestmentPlanned;

    String status; //PLANNING, IMPLEMENTING, CLOSED

    String startPlanDate;

    String endPlanDate;

    String description;

    @Column(name = "is_approved")
    @Builder.Default
    Boolean isApproved = false;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private Set<ProjectCapitalAllocation> capitalAllocations = new HashSet<>();

}
