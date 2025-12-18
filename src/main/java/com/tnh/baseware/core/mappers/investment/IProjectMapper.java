package com.tnh.baseware.core.mappers.investment;

import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.entities.investment.Project;
import com.tnh.baseware.core.forms.investment.ProjectEditorForm;
import com.tnh.baseware.core.mappers.IGenericMapper;
import com.tnh.baseware.core.mappers.adu.IOrganizationMapper;
import com.tnh.baseware.core.repositories.adu.IOrganizationRepository;
import com.tnh.baseware.core.repositories.audit.ICategoryRepository;
import com.tnh.baseware.core.repositories.investment.IIndustryRepository;
import com.tnh.baseware.core.mappers.audit.ICategoryMapper;
import com.tnh.baseware.core.mappers.investment.capital.ICapitalAllocationMapper;
import com.tnh.baseware.core.dtos.investment.project.ProjectDTO;
import com.tnh.baseware.core.dtos.investment.project.ReportProjectDTO;
import com.tnh.baseware.core.dtos.investment.project.ProjectCheckAprroveDTO;
import com.tnh.baseware.core.dtos.investment.capital.ReportCapitalAllocationDTO;
import com.tnh.baseware.core.dtos.investment.capital.ReportCapitalAllocationDetailDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.Collections;
import java.util.stream.Collectors;

import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
uses = {
        IIndustryMapper.class,
        ICategoryMapper.class,
        IOrganizationMapper.class,
        ICapitalAllocationMapper.class
})
public interface IProjectMapper extends IGenericMapper<Project, ProjectEditorForm, ProjectDTO> {

        @Mapping(target = "industry", expression = "java(fetcher.formToEntity(industryRepository, form.getIndustryId()))")
        @Mapping(target = "group", expression = "java(fetcher.formToEntity(categoryRepository, form.getGroupId()))")
        @Mapping(target = "projectLevel", expression = "java(fetcher.formToEntity(categoryRepository, form.getProjectLevelId()))")
        @Mapping(target = "ownerOrg", expression = "java(fetcher.formToEntity(organizationRepository, form.getOwnerOrgId()))")
        @Mapping(target = "pmuOrg", expression = "java(fetcher.formToEntity(organizationRepository, form.getPmuOrgId()))")
        @Mapping(target = "admOrg", expression = "java(fetcher.formToEntity(organizationRepository, form.getAdmOrgId()))")
        Project formToEntity(ProjectEditorForm form,
                        @Context GenericEntityFetcher fetcher,
                        @Context IIndustryRepository industryRepository,
                        @Context ICategoryRepository categoryRepository,
                        @Context IOrganizationRepository organizationRepository);

        @Mapping(target = "industry", expression = "java(fetcher.formToEntity(industryRepository, form.getIndustryId()))")
        @Mapping(target = "group", expression = "java(fetcher.formToEntity(categoryRepository, form.getGroupId()))")
        @Mapping(target = "projectLevel", expression = "java(fetcher.formToEntity(categoryRepository, form.getProjectLevelId()))")
        @Mapping(target = "ownerOrg", expression = "java(fetcher.formToEntity(organizationRepository, form.getOwnerOrgId()))")
        @Mapping(target = "pmuOrg", expression = "java(fetcher.formToEntity(organizationRepository, form.getPmuOrgId()))")
        @Mapping(target = "admOrg", expression = "java(fetcher.formToEntity(organizationRepository, form.getAdmOrgId()))")
        void updateProjectFromForm(ProjectEditorForm form, @MappingTarget Project project,
                        @Context GenericEntityFetcher fetcher,
                        @Context IIndustryRepository industryRepository,
                        @Context ICategoryRepository categoryRepository,
                        @Context IOrganizationRepository organizationRepository);

        List<ProjectCheckAprroveDTO> toListProjectCheckAprroveDTOs(List<Project> projects);

        default List<ReportProjectDTO> toListReportProjectDTOs(List<Project> projects,
                                                           Map<UUID, BigDecimal> disbursementMap,
                                                           Map<UUID, BigDecimal> executionMap) {
                if (projects == null) return Collections.emptyList();

                return projects.stream().map(project -> {
                List<ReportCapitalAllocationDTO> capitalAllocations = project.getCapitalAllocations().stream().map(cap -> {
                        List<ReportCapitalAllocationDetailDTO> detailDTOs = cap.getCapitalAllocationDetails().stream().map(detail ->
                                ReportCapitalAllocationDetailDTO.builder()
                                        .id(detail.getId())
                                        .amount(detail.getAmount())
                                        .allocationDate(detail.getAllocationDate())
                                        .totalDisbursement(disbursementMap.get(detail.getId()))
                                        .totalAllocationExecution(executionMap.get(detail.getId()))
                                        .build()
                        ).collect(Collectors.toList());

                        return ReportCapitalAllocationDTO.builder()
                                .id(cap.getId())
                                .amount(cap.getAmount())
                                .capitalAllocationDetails(detailDTOs)
                                .build();
                }).collect(Collectors.toList());

                return ReportProjectDTO.builder()
                        .id(project.getId())
                        .name(project.getName())
                        .capitalAllocations(capitalAllocations)
                        .build();

                }).collect(Collectors.toList());
        }

}
