package com.tnh.baseware.core.repositories.investment.history;

import com.tnh.baseware.core.entities.investment.history.DisbursementHistory;
import com.tnh.baseware.core.repositories.IGenericRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IDisbursementHistoryRepository extends IGenericRepository<DisbursementHistory, UUID> {
}
