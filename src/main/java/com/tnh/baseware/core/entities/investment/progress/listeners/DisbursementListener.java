package com.tnh.baseware.core.entities.investment.progress.listeners;

import com.tnh.baseware.core.components.ApplicationContextProvider;
import com.tnh.baseware.core.entities.investment.history.DisbursementHistory;
import com.tnh.baseware.core.entities.investment.progress.Disbursement;
import com.tnh.baseware.core.repositories.investment.history.IDisbursementHistoryRepository;

import jakarta.persistence.*;

import com.tnh.baseware.core.enums.ActionType;

public class DisbursementListener {

    @PostPersist
    public void postPersist(Disbursement entity) {
        saveHistory(entity, ActionType.INSERT);
    }

    @PostUpdate
    public void postUpdate(Disbursement entity) {
        saveHistory(entity, ActionType.UPDATE);
    }

    @PostRemove
    public void postRemove(Disbursement entity) {
        saveHistory(entity, ActionType.DELETE);
    }

    private void saveHistory(Disbursement entity, ActionType action) {
        IDisbursementHistoryRepository repository = ApplicationContextProvider.getBean(IDisbursementHistoryRepository.class);
        
        DisbursementHistory history = DisbursementHistory.builder()
                .originalId(entity.getId())
                .actionType(action)
                .code(entity.getCode())
                .investmentItem(entity.getInvestmentItem())
                .amount(entity.getAmount())
                .disbursementDate(entity.getDisbursementDate())
                .responsiblePerson(entity.getResponsiblePerson())
                .voucherNumber(entity.getVoucherNumber())
                .description(entity.getDescription())
                .isApproved(entity.getIsApproved())
                .disbursementType(entity.getDisbursementType())
                .project(entity.getProject())
                .capitalPlanLine(entity.getCapitalPlanLine())
                .build();

        history.setCreatedBy(entity.getCreatedBy());
        
        repository.save(history);
    }
}
