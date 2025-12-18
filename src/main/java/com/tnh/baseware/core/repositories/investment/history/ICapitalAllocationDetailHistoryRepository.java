package com.tnh.baseware.core.repositories.investment.history;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.tnh.baseware.core.repositories.IGenericRepository;
import com.tnh.baseware.core.entities.investment.history.CapitalAllocationDetailHistory;

@Repository
public interface ICapitalAllocationDetailHistoryRepository extends IGenericRepository<CapitalAllocationDetailHistory, UUID> {

}
