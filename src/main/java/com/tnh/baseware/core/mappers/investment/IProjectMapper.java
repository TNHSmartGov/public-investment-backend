package com.tnh.baseware.core.mappers.investment;

import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.entities.investment.Project;
import com.tnh.baseware.core.forms.investment.ProjectEditorForm;
import com.tnh.baseware.core.mappers.IGenericMapper;
import com.tnh.baseware.core.repositories.adu.IOrganizationRepository;
import com.tnh.baseware.core.repositories.audit.ICategoryRepository;
import com.tnh.baseware.core.repositories.investment.IIndustryRepository;
import com.tnh.baseware.core.repositories.investment.IProjectRepository;
import com.tnh.baseware.core.dtos.investment.project.ProjectDTO;

import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IProjectMapper extends IGenericMapper<Project, ProjectEditorForm, ProjectDTO> {

    @Mapping(target = "parentProject", expression = "java(form.getParentProjectId() != null ? fetcher.formToEntity(projectRepository, form.getParentProjectId()) : null)")
    @Mapping(target = "industry", expression = "java(fetcher.formToEntity(industryRepository, form.getIndustryId()))")
    @Mapping(target = "group", expression = "java(fetcher.formToEntity(categoryRepository, form.getGroupId()))")
    @Mapping(target = "projectLevel", expression = "java(form.getProjectLevelId() != null ? fetcher.formToEntity(categoryRepository, form.getProjectLevelId()) : null)")
    @Mapping(target = "ownerOrg", expression = "java(fetcher.formToEntity(organizationRepository, form.getOwnerOrgId()))")
    @Mapping(target = "pmuOrg", expression = "java(fetcher.formToEntity(organizationRepository, form.getPmuOrgId()))")
    @Mapping(target = "admOrg", expression = "java(fetcher.formToEntity(organizationRepository, form.getAdmOrgId()))")
    @Mapping(target = "approvalDecisions", ignore = true)
    @Mapping(target = "bidPlans", ignore = true)
    @Mapping(target = "packageBids", ignore = true)
    @Mapping(target = "constructionSites", ignore = true)
    @Mapping(target = "landClearances", ignore = true)
    Project formToEntityWithContext(ProjectEditorForm form,
                         @Context GenericEntityFetcher fetcher,
                         @Context IProjectRepository projectRepository,
                         @Context IIndustryRepository industryRepository,
                         @Context ICategoryRepository categoryRepository,
                         @Context IOrganizationRepository organizationRepository);

    @Mapping(target = "parentProjectId", source = "parentProject.id")
    @Mapping(target = "parentProjectName", source = "parentProject.name")
    @Mapping(target = "industryName", source = "industry.name")
    @Mapping(target = "groupName", source = "group.name")
    @Mapping(target = "projectLevelName", source = "projectLevel.name")
    @Mapping(target = "ownerOrgName", source = "ownerOrg.name")
    @Mapping(target = "pmuOrgName", source = "pmuOrg.name")
    @Mapping(target = "admOrgName", source = "admOrg.name")
    ProjectDTO entityToDTO(Project entity);

    // Kế thừa cấu hình từ phương thức có tên rõ ràng là "formToEntityWithContext"
    @InheritConfiguration(name = "formToEntityWithContext")
    void updateProjectFromForm(ProjectEditorForm form, 
                        @MappingTarget Project entity, 
                        @Context GenericEntityFetcher fetcher,
                        @Context IProjectRepository projectRepository,
                        @Context IIndustryRepository industryRepository,
                        @Context ICategoryRepository categoryRepository,
                        @Context IOrganizationRepository organizationRepository);
}
