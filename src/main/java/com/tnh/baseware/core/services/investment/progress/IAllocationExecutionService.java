package com.tnh.baseware.core.services.investment.progress;

import java.util.List;
import java.util.UUID;

import com.tnh.baseware.core.dtos.investment.progress.AllocationExecutionDTO;
import com.tnh.baseware.core.entities.investment.progress.AllocationExecution;
import com.tnh.baseware.core.forms.investment.progress.AllocationExecutionEditorForm;
import com.tnh.baseware.core.services.IGenericService;

public interface IAllocationExecutionService extends
        IGenericService<AllocationExecution, AllocationExecutionEditorForm, AllocationExecutionDTO, UUID> {

        List<AllocationExecutionDTO> getListByProjectId(UUID id);
}

