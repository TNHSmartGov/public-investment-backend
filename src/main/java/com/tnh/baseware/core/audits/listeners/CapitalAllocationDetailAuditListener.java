package com.tnh.baseware.core.audits.listeners;

import java.time.LocalDateTime;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

import com.tnh.baseware.core.components.SpringContext;
import com.tnh.baseware.core.entities.investment.capital.CapitalAllocationDetail;
import com.tnh.baseware.core.entities.investment.history.CapitalAllocationDetailHistory;
import com.tnh.baseware.core.repositories.investment.history.ICapitalAllocationDetailHistoryRepository;

public class CapitalAllocationDetailAuditListener {

    @PrePersist
    public void prePersist(CapitalAllocationDetail entity) {
        saveHistory(entity, "INSERT");
    }

    @PreUpdate
    public void preUpdate(CapitalAllocationDetail entity) {
        saveHistory(entity, "UPDATE");
    }

    @PreRemove
    public void preRemove(CapitalAllocationDetail entity) {
        saveHistory(entity, "DELETE");
    }

    private void saveHistory(CapitalAllocationDetail entity, String action) {
        System.out.println("prePersist called");
        CapitalAllocationDetailHistory history = new CapitalAllocationDetailHistory();
        history.setCapitalAllocationId(entity.getCapitalAllocation().getId());
        history.setCapitalAllocationDetailId(entity.getId());
        history.setAmount(entity.getAmount());
        history.setDescription(entity.getDescription());
        history.setCreatedDate(entity.getCreatedDate());
        history.setAction(action);
        history.setModifiedDate(LocalDateTime.now());
        history.setModifiedBy(entity.getModifiedBy());

        // Save via Spring context
        SpringContext.getBean(ICapitalAllocationDetailHistoryRepository.class)
                     .save(history);
    }
}
