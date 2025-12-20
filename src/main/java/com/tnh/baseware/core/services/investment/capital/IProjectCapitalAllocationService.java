package com.tnh.baseware.core.services.investment.capital;

import java.util.UUID;

import com.tnh.baseware.core.dtos.investment.capital.ProjectCapitalAllocationDTO;
import com.tnh.baseware.core.entities.investment.capital.ProjectCapitalAllocation;
import com.tnh.baseware.core.forms.investment.capital.ProjectCapitalAllocationEditorForm;
import com.tnh.baseware.core.services.IGenericService;

public interface IProjectCapitalAllocationService extends
        IGenericService<ProjectCapitalAllocation, ProjectCapitalAllocationEditorForm, ProjectCapitalAllocationDTO, UUID> {
}
