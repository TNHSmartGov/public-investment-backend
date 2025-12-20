package com.tnh.baseware.core.services.investment;

import java.util.UUID;

import com.tnh.baseware.core.dtos.investment.project.ProjectDTO;
import com.tnh.baseware.core.entities.investment.Project;
import com.tnh.baseware.core.forms.investment.ProjectEditorForm;
import com.tnh.baseware.core.services.IGenericService;

public interface IProjectService extends
        IGenericService<Project, ProjectEditorForm, ProjectDTO, UUID> {
}
