package com.tnh.baseware.core.forms.investment;

import java.math.BigDecimal;
import java.util.UUID;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProjectEditorForm {

    UUID id;

    @NotBlank(message = "Mã dự án không được để trống")
    String code;

    @NotBlank(message = "Tên dự án không được để trống")
    String name;

    UUID parentProjectId; // ID của dự án cha hoặc gói vốn tổng
    Boolean isProgram;   // TRUE: Gói vốn, FALSE: Dự án cụ thể

    @NotNull(message = "Ngành không được để trống")
    UUID industryId;

    @NotNull(message = "Nhóm dự án không được để trống")
    UUID groupId;

    UUID projectLevelId;

    @NotNull(message = "Chủ đầu tư không được để trống")
    UUID ownerOrgId;

    @NotNull(message = "Đơn vị QLDA không được để trống")
    UUID pmuOrgId;

    @NotNull(message = "Địa bàn không được để trống")
    UUID admOrgId;

    BigDecimal totalInvestmentPlanned;
    String status;
    String startPlanDate;
    String endPlanDate;
    String description;
    Boolean isApproved;
}
