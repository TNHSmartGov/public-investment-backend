package com.tnh.baseware.core.services.investment.history;

import java.util.UUID;

import com.tnh.baseware.core.dtos.investment.history.AllocationExecutionHistoryDTO;
import com.tnh.baseware.core.entities.investment.history.AllocationExecutionHistory;
import com.tnh.baseware.core.forms.investment.history.AllocationExecutionHistoryEditorForm;
import com.tnh.baseware.core.services.IGenericService;

public interface IAllocationExecutionHistoryService extends
        IGenericService<AllocationExecutionHistory, AllocationExecutionHistoryEditorForm, AllocationExecutionHistoryDTO, UUID> {
}
