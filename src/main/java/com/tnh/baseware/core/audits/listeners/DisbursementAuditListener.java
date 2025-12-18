package com.tnh.baseware.core.audits.listeners;

import java.time.Instant;
import java.time.LocalDateTime;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

import com.tnh.baseware.core.components.SpringContext;
import com.tnh.baseware.core.entities.investment.history.DisbursementHistory;
import com.tnh.baseware.core.entities.investment.progress.Disbursement;
import com.tnh.baseware.core.repositories.investment.history.IDisbursementHistoryRepository;

public class DisbursementAuditListener {

    @PrePersist
    public void prePersist(Disbursement entity) {
        saveHistory(entity, "INSERT");
    }

    @PreUpdate
    public void preUpdate(Disbursement entity) {
        saveHistory(entity, "UPDATE");
    }

    @PreRemove
    public void preRemove(Disbursement entity) {
        saveHistory(entity, "DELETE");
    }

    private void saveHistory(Disbursement entity, String action) {
        DisbursementHistory history = new DisbursementHistory();
        history.setCapitalAllocationDetailId(entity.getId());
        history.setAmount(entity.getAmount());
        history.setDescription(entity.getDescription());
        history.setCreatedDate(entity.getCreatedDate());
        history.setResponsiblePerson(entity.getResponsiblePerson());
        history.setModifiedDate(Instant.now());
        history.setModifiedBy(entity.getModifiedBy());
        history.setAction(action);
        history.setDisbursementDate(entity.getDisbursementDate());

        // Save via Spring context
        SpringContext.getBean(IDisbursementHistoryRepository.class)
                     .save(history);
    }
}
