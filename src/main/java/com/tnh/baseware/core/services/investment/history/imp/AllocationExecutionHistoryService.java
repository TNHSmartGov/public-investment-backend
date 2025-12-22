package com.tnh.baseware.core.services.investment.history.imp;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.dtos.investment.history.AllocationExecutionHistoryDTO;
import com.tnh.baseware.core.entities.investment.history.AllocationExecutionHistory;
import com.tnh.baseware.core.forms.investment.history.AllocationExecutionHistoryEditorForm;
import com.tnh.baseware.core.mappers.investment.history.IAllocationExecutionHistoryMapper;
import com.tnh.baseware.core.repositories.investment.history.IAllocationExecutionHistoryRepository;
import com.tnh.baseware.core.services.GenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.investment.history.IAllocationExecutionHistoryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AllocationExecutionHistoryService extends
        GenericService<AllocationExecutionHistory, AllocationExecutionHistoryEditorForm, AllocationExecutionHistoryDTO, IAllocationExecutionHistoryRepository, IAllocationExecutionHistoryMapper, UUID>
        implements IAllocationExecutionHistoryService {

    public AllocationExecutionHistoryService(IAllocationExecutionHistoryRepository repository,
            IAllocationExecutionHistoryMapper mapper, MessageService messageService) {
        super(repository, mapper, messageService, AllocationExecutionHistory.class);
    }

}
