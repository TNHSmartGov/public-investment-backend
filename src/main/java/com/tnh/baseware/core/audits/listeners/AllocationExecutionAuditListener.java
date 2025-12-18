package com.tnh.baseware.core.audits.listeners;

import java.time.Instant;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

import com.tnh.baseware.core.components.SpringContext;
import com.tnh.baseware.core.entities.investment.history.AllocationExecutionHistory;
import com.tnh.baseware.core.entities.investment.progress.AllocationExecution;
import com.tnh.baseware.core.repositories.investment.history.IAllocationExecutionHistoryRepository;

public class AllocationExecutionAuditListener {

    @PrePersist
    public void prePersist(AllocationExecution entity) {
        saveHistory(entity, "INSERT");
    }

    @PreUpdate
    public void preUpdate(AllocationExecution entity) {
        saveHistory(entity, "UPDATE");
    }

    @PreRemove
    public void preRemove(AllocationExecution entity) {
        saveHistory(entity, "DELETE");
    }

    private void saveHistory(AllocationExecution entity, String action) {
        AllocationExecutionHistory history = new AllocationExecutionHistory();
        history.setCapitalAllocationDetailId(entity.getId());
        history.setAmount(entity.getAmount());
        history.setDescription(entity.getDescription());
        history.setCreatedDate(entity.getCreatedDate());
        history.setResponsiblePerson(entity.getResponsiblePerson());
        history.setModifiedDate(Instant.now());
        history.setModifiedBy(entity.getModifiedBy());
        history.setAction(action);
        history.setExecutionDate(entity.getExecutionDate());

        // Save via Spring context
        SpringContext.getBean(IAllocationExecutionHistoryRepository.class)
                     .save(history);
    }
}
