package com.tnh.baseware.core.repositories.investment.capital;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tnh.baseware.core.entities.investment.capital.CapitalAllocationDetail;
import com.tnh.baseware.core.repositories.IGenericRepository;

@Repository
public interface ICapitalAllocationDetailRepository extends IGenericRepository<CapitalAllocationDetail, UUID> {

    @Query("""
        SELECT COALESCE(SUM(d.amount), 0)
        FROM CapitalAllocationDetail d
        WHERE d.deleted = false AND d.capitalAllocation.id = :id
        GROUP BY d.capitalAllocation.id
    """)
    BigDecimal findTotalAmountByCapitalAllocationId(UUID id);

}
