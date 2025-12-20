package com.tnh.baseware.core.services.investment.imp;

import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.dtos.investment.project.ProjectDTO;
import com.tnh.baseware.core.entities.investment.Project;
import com.tnh.baseware.core.exceptions.BWCNotFoundException;
import com.tnh.baseware.core.exceptions.BWCValidationException;
import com.tnh.baseware.core.forms.investment.ProjectEditorForm;
import com.tnh.baseware.core.mappers.investment.IProjectMapper;
import com.tnh.baseware.core.repositories.investment.IProjectRepository;
import com.tnh.baseware.core.repositories.investment.IIndustryRepository;
import com.tnh.baseware.core.repositories.adu.IOrganizationRepository;
import com.tnh.baseware.core.repositories.audit.ICategoryRepository;
import com.tnh.baseware.core.services.GenericService;
import com.tnh.baseware.core.services.MessageService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tnh.baseware.core.services.investment.IProjectService;

@Slf4j
@Service
public class ProjectServiceImpl extends
        GenericService<Project, ProjectEditorForm, ProjectDTO, IProjectRepository, IProjectMapper, UUID>
        implements IProjectService {

    private final IProjectRepository projectRepository;
    private final IIndustryRepository industryRepository;
    private final ICategoryRepository categoryRepository;
    private final IOrganizationRepository organizationRepository;
    private final GenericEntityFetcher fetcher;

    public ProjectServiceImpl(IProjectRepository repository,
                              IProjectMapper mapper,
                              MessageService messageService,
                              IIndustryRepository industryRepository,
                              ICategoryRepository categoryRepository,
                              IOrganizationRepository organizationRepository,
                              GenericEntityFetcher fetcher) {
        // Truyền các thành phần cơ bản lên GenericService
        super(repository, mapper, messageService, Project.class);
        this.projectRepository = repository;
        this.industryRepository = industryRepository;
        this.categoryRepository = categoryRepository;
        this.organizationRepository = organizationRepository;
        this.fetcher = fetcher;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ProjectDTO create(ProjectEditorForm form) {
        // 1. Kiểm tra quy tắc phân cấp dự án
        validateProjectHierarchy(form);

        // 2. Chuyển đổi Form sang Entity (Sử dụng phương thức có đầy đủ Context)
        var project = mapper.formToEntityWithContext(form, fetcher, projectRepository, 
                industryRepository, categoryRepository, organizationRepository);

        // 3. Lưu và trả về DTO
        return mapper.entityToDTO(repository.save(project));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ProjectDTO update(UUID id, ProjectEditorForm form) {
        // 1. Tìm dự án hiện tại
        var project = repository.findById(id)
                .orElseThrow(() -> new BWCNotFoundException(messageService.getMessage("project.not.found", id)));

        // 2. Kiểm tra quy tắc phân cấp
        validateProjectHierarchy(form);

        // 3. Cập nhật dữ liệu vào Entity hiện có
        mapper.updateProjectFromForm(form, project, fetcher, projectRepository, 
                industryRepository, categoryRepository, organizationRepository);
        
        return mapper.entityToDTO(repository.save(project));
    }

    /**
     * Logic nghiệp vụ: 
     * - Nếu dự án có cha, thì dự án cha đó phải được đánh dấu là Chương trình/Gói vốn (isProgram = true).
     * - Một dự án không thể vừa là con của chính nó.
     */
    private void validateProjectHierarchy(ProjectEditorForm form) {
        if (form.getParentProjectId() != null) {
            // Kiểm tra trùng ID (tránh vòng lặp vô hạn)
            if (form.getId() != null && form.getId().equals(form.getParentProjectId())) {
                throw new BWCValidationException(messageService.getMessage("project.hierarchy.self_parent"));
            }

            // Kiểm tra tính chất của dự án cha
            Project parent = projectRepository.findById(form.getParentProjectId())
                    .orElseThrow(() -> new BWCNotFoundException(messageService.getMessage("project.parent.not.found")));

            if (Boolean.FALSE.equals(parent.getIsProgram())) {
                throw new BWCValidationException(messageService.getMessage("project.parent.must_be_program"));
            }
        }
    }
}
