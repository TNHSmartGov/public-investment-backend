package com.tnh.baseware.core.entities.investment.progress.listeners;

import com.tnh.baseware.core.components.ApplicationContextProvider;
import com.tnh.baseware.core.entities.investment.history.AllocationExecutionHistory;
import com.tnh.baseware.core.entities.investment.progress.AllocationExecution;
import com.tnh.baseware.core.repositories.investment.history.IAllocationExecutionHistoryRepository;

import jakarta.persistence.*;

import com.tnh.baseware.core.enums.ActionType;

public class AllocationExecutionListener {

    @PostPersist
    public void postPersist(AllocationExecution entity) {
        saveHistory(entity, ActionType.INSERT);
    }

    @PostUpdate
    public void postUpdate(AllocationExecution entity) {
        saveHistory(entity, ActionType.UPDATE);
    }

    @PostRemove
    public void postRemove(AllocationExecution entity) {
        saveHistory(entity, ActionType.DELETE);
    }

    private void saveHistory(AllocationExecution entity, ActionType action) {
        IAllocationExecutionHistoryRepository repository = ApplicationContextProvider.getBean(IAllocationExecutionHistoryRepository.class);
        
        AllocationExecutionHistory history = AllocationExecutionHistory.builder()
                .originalId(entity.getId())
                .actionType(action)
                .code(entity.getCode())
                .investmentItem(entity.getInvestmentItem())
                .amount(entity.getAmount())
                .executionDate(entity.getExecutionDate())
                .responsiblePerson(entity.getResponsiblePerson())
                .description(entity.getDescription())
                .isApproved(entity.getIsApproved())
                .executionType(entity.getExecutionType())
                .project(entity.getProject())
                .capitalPlanLine(entity.getCapitalPlanLine())
                .build();
        
        history.setCreatedBy(entity.getCreatedBy());
        
        repository.save(history);
    }
}
