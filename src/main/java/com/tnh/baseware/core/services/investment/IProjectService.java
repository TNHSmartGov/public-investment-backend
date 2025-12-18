package com.tnh.baseware.core.services.investment;

import java.util.UUID;
import java.util.List;

import com.tnh.baseware.core.dtos.investment.project.ProjectDTO;
import com.tnh.baseware.core.dtos.investment.project.ProjectCheckAprroveDTO;
import com.tnh.baseware.core.entities.investment.Project;
import com.tnh.baseware.core.forms.investment.ProjectEditorForm;
import com.tnh.baseware.core.services.IGenericService;

public interface IProjectService extends
        IGenericService<Project, ProjectEditorForm, ProjectDTO, UUID> {

        @Override
        ProjectDTO create(ProjectEditorForm form);

        @Override
        ProjectDTO update(UUID id, ProjectEditorForm form);

        boolean approve(UUID id);

        boolean reject(UUID id);

        List<ProjectCheckAprroveDTO> getListCheckApprove();

        List<ProjectDTO> getListProjectByOwners(List<UUID> ownerIds);

}
