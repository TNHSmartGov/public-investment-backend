package com.tnh.baseware.core.audits.listeners;

import java.time.LocalDateTime;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

import com.tnh.baseware.core.components.SpringContext;
import com.tnh.baseware.core.entities.investment.capital.CapitalAllocation;
import com.tnh.baseware.core.entities.investment.history.CapitalAllocationHistory;
import com.tnh.baseware.core.repositories.investment.history.ICapitalAllocationHistoryRepository;


public class CapitalAllocationAuditListener {

    @PrePersist
    public void prePersist(CapitalAllocation entity) {
        saveHistory(entity, "INSERT");
    }

    @PreUpdate
    public void preUpdate(CapitalAllocation entity) {
        saveHistory(entity, "UPDATE");
    }

    @PreRemove
    public void preRemove(CapitalAllocation entity) {
        saveHistory(entity, "DELETE");
    }

    private void saveHistory(CapitalAllocation entity, String action) {
        CapitalAllocationHistory history = new CapitalAllocationHistory();
        history.setProjectId(entity.getProject().getId());
        history.setCapitalAllocationId(entity.getId());
        history.setAmount(entity.getAmount());
        history.setDescription(entity.getDescription());
        history.setCreatedDate(entity.getCreatedDate());
        history.setAction(action);
        history.setModifiedDate(LocalDateTime.now());
        history.setModifiedBy(entity.getModifiedBy());

        // Save via Spring context
        SpringContext.getBean(ICapitalAllocationHistoryRepository.class)
                     .save(history);
    }
}
