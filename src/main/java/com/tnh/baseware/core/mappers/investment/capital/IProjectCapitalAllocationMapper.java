package com.tnh.baseware.core.mappers.investment.capital;

import org.mapstruct.*;

import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.mappers.IGenericMapper;
import com.tnh.baseware.core.repositories.investment.capital.ICapitalPlanRepository;
import com.tnh.baseware.core.dtos.investment.capital.ProjectCapitalAllocationDTO;
import com.tnh.baseware.core.entities.investment.capital.ProjectCapitalAllocation;
import com.tnh.baseware.core.repositories.investment.IProjectRepository;
import com.tnh.baseware.core.forms.investment.capital.ProjectCapitalAllocationEditorForm;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { ICapitalPlanLineMapper.class })
public interface IProjectCapitalAllocationMapper extends IGenericMapper<ProjectCapitalAllocation, ProjectCapitalAllocationEditorForm, ProjectCapitalAllocationDTO> {

    @Mapping(target = "project", expression = "java(fetcher.formToEntity(projectRepository, form.getProjectId()))")
    @Mapping(target = "capitalPlan", expression = "java(fetcher.formToEntity(capitalPlanRepository, form.getCapitalPlanId()))")
    @Mapping(target = "capitalPlanLines", ignore = true) // Không ghi đè danh sách con khi map từ Form
    ProjectCapitalAllocation formToEntity(ProjectCapitalAllocationEditorForm form,
                                         @Context GenericEntityFetcher fetcher,
                                         @Context IProjectRepository projectRepository,
                                         @Context ICapitalPlanRepository capitalPlanRepository);

    @Mapping(target = "project", expression = "java(fetcher.formToEntity(projectRepository, form.getProjectId()))")
    @Mapping(target = "capitalPlan", expression = "java(fetcher.formToEntity(capitalPlanRepository, form.getCapitalPlanId()))")
    @Mapping(target = "capitalPlanLines", ignore = true)
    void updateFromForm(ProjectCapitalAllocationEditorForm form, 
                        @MappingTarget ProjectCapitalAllocation entity,
                        @Context GenericEntityFetcher fetcher,
                        @Context IProjectRepository projectRepository,
                        @Context ICapitalPlanRepository capitalPlanRepository);

    @Mapping(target = "capitalPlanLines", source = "capitalPlanLines", qualifiedByName = "planLineToDTO")
    ProjectCapitalAllocationDTO entityToDTO(ProjectCapitalAllocation entity);
}