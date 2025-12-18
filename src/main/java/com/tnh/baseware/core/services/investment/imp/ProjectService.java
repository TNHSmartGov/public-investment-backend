package com.tnh.baseware.core.services.investment.imp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import com.tnh.baseware.core.components.EnumRegistry;
import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.dtos.investment.project.ProjectCheckAprroveDTO;
import com.tnh.baseware.core.dtos.investment.project.ProjectDTO;
import com.tnh.baseware.core.entities.adu.Organization;
import com.tnh.baseware.core.entities.investment.Project;
import com.tnh.baseware.core.entities.user.User;
import com.tnh.baseware.core.enums.OrganizationLevel;
import com.tnh.baseware.core.exceptions.BWCNotFoundException;
import com.tnh.baseware.core.forms.investment.ProjectEditorForm;
import com.tnh.baseware.core.mappers.investment.IProjectMapper;
import com.tnh.baseware.core.repositories.investment.IProjectRepository;
import com.tnh.baseware.core.repositories.investment.capital.ICapitalAllocationRepository;
import com.tnh.baseware.core.repositories.investment.IIndustryRepository;
import com.tnh.baseware.core.repositories.adu.IOrganizationRepository;
import com.tnh.baseware.core.repositories.audit.ICategoryRepository;
import com.tnh.baseware.core.services.GenericService;
import com.tnh.baseware.core.services.MessageService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.tnh.baseware.core.services.investment.IProjectService;
import com.tnh.baseware.core.services.investment.capital.imp.CapitalAllocationService;
import com.tnh.baseware.core.specs.GenericSpecification;
import com.tnh.baseware.core.specs.SearchRequest;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProjectService extends
        GenericService<Project, ProjectEditorForm, ProjectDTO, IProjectRepository, IProjectMapper, UUID>
        implements
        IProjectService {

    IProjectRepository repository;
    IProjectMapper mapper;
    IOrganizationRepository organizationRepository;
    ICategoryRepository categoryRepository;
    IIndustryRepository industryRepository;
    ICapitalAllocationRepository capitalAllocationRepository;
    GenericEntityFetcher fetcher;

    public ProjectService(IProjectRepository repository,
            IProjectMapper mapper,
            IIndustryRepository industryRepository,
            EnumRegistry enumRegistry,
            ICategoryRepository categoryRepository,
            IOrganizationRepository organizationRepository,
            ICapitalAllocationRepository capitalAllocationRepository,
            MessageService messageService, CapitalAllocationService capitalAllocationsService, GenericEntityFetcher fetcher) {
        super(repository, mapper, messageService, Project.class, enumRegistry);
        this.repository = repository;
        this.organizationRepository = organizationRepository;
        this.categoryRepository = categoryRepository;
        this.industryRepository = industryRepository;
        this.mapper = mapper;
        this.fetcher = fetcher;
        this.capitalAllocationRepository = capitalAllocationRepository;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ProjectDTO create(ProjectEditorForm form) {
        var project = mapper.formToEntity(form, fetcher, industryRepository, categoryRepository,
                organizationRepository);
        return mapper.entityToDTO(repository.save(project));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ProjectDTO update(UUID id, ProjectEditorForm form) {
        var project = repository.findById(id)
                .orElseThrow(() -> new BWCNotFoundException(messageService.getMessage("project.not.found", id)));
        mapper.updateProjectFromForm(form, project, fetcher, industryRepository, categoryRepository,
                organizationRepository);
        return mapper.entityToDTO(repository.save(project));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void delete(UUID id) {
        var project = repository.findById(id)
                .orElseThrow(() -> new BWCNotFoundException(messageService.getMessage("project.not.found", id)));
        
        capitalAllocationRepository.deleteAll(capitalAllocationRepository.findByProjectId(id));

        repository.delete(project);
    }

    public boolean approve(UUID id) {
        Project project = repository.findById(id).orElseThrow(
                                () -> new BWCNotFoundException(messageService.getMessage("project.not.found", id)));
        project.setIsApproved(true);
        repository.save(project);
        return true;
    }

    public boolean reject(UUID id) {
        Project project = repository.findById(id).orElseThrow(
                                () -> new BWCNotFoundException(messageService.getMessage("project.not.found", id)));
        project.setIsApproved(false);
        repository.save(project);
        return true;
    }

    public List<ProjectCheckAprroveDTO> getListCheckApprove()
    {
        List<Project> projects = repository.findAllByDeletedFalse();
        List<ProjectCheckAprroveDTO> projectDTOs =  mapper.toListProjectCheckAprroveDTOs(projects);
        return projectDTOs;
    }

    public List<ProjectDTO> getListProjectByOwners(List<UUID> ownerIds) {
        List<ProjectDTO> listProjectDTOs = new ArrayList();
        
        if (ownerIds == null || ownerIds.isEmpty()) {
            return listProjectDTOs;
        }
        
        return mapper.entitiesToDTOs(repository.getListProjectByOwners(ownerIds));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectDTO> findAll(Pageable pageable) {

        User currentUser = getCurrentUser();

        Page<Project> projects;

        if (Boolean.TRUE.equals(currentUser.getCanViewAll())) {
            // có quyền xem tất cả
            projects = repository.findAll(pageable);
        } else {
            // lọc các organization của user có cấp OWNER
            List<Organization> ownerOrgs = currentUser.getOrganizations().stream()
                    .filter(org -> org.getLevel() == OrganizationLevel.OWNER.getValue())
                    .collect(Collectors.toList());

            if (ownerOrgs.isEmpty()) {
                projects = Page.empty(pageable);
            } else {
                projects = repository.findByOwnerOrgIn(ownerOrgs, pageable);
            }
        }

        return projects.map(mapper::entityToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDTO> findAll() {

        User currentUser = getCurrentUser();

        List<Project> projects;

        if (Boolean.TRUE.equals(currentUser.getCanViewAll())) {
            // có quyền xem tất cả
            projects = repository.findAll();
        } else {
            // lọc các organization của user có cấp OWNER
            List<Organization> ownerOrgs = currentUser.getOrganizations().stream()
                    .filter(org -> org.getLevel() == OrganizationLevel.OWNER.getValue())
                    .collect(Collectors.toList());

            if (ownerOrgs.isEmpty()) {
                projects = new ArrayList<>();
            } else {
                projects = repository.findByOwnerOrgIn(ownerOrgs);
            }
        }

        return mapper.entitiesToDTOs(projects);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectDTO> search(SearchRequest searchRequest) {
        var specification = new GenericSpecification<Project>(searchRequest);
        var pageable = GenericSpecification.getPageable(searchRequest.getPage(), searchRequest.getSize());

        User currentUser = getCurrentUser();

        Page<Project> projects;

        if (Boolean.TRUE.equals(currentUser.getCanViewAll())) {
            // có quyền xem tất cả
            projects = repository.findAll(specification, pageable);
        } else {
            // lọc các organization của user có cấp OWNER
            List<Organization> ownerOrgs = currentUser.getOrganizations().stream()
                    .filter(org -> org.getLevel() == OrganizationLevel.OWNER.getValue())
                    .collect(Collectors.toList());

            if (ownerOrgs.isEmpty()) {
                projects = Page.empty(pageable);
            } else {
                Specification<Project> byOwnerOrgs = (root, query, cb) ->
                root.get("ownerOrg").in(ownerOrgs);
                
                projects = repository.findAll(Specification.where(specification).and(byOwnerOrgs), pageable);
            }
        }

        return projects.map(mapper::entityToDTO);
    }
}
