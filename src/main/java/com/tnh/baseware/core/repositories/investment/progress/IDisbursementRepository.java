package com.tnh.baseware.core.repositories.investment.progress;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tnh.baseware.core.repositories.IGenericRepository;
import com.tnh.baseware.core.entities.investment.progress.Disbursement;

@Repository
public interface IDisbursementRepository extends IGenericRepository<Disbursement, UUID> {

    @Query("""
        SELECT d
        FROM Disbursement d
        JOIN CapitalAllocationDetail cad ON d.capitalAllocationDetail.id = cad.id
        JOIN CapitalAllocation ca ON cad.capitalAllocation.id = ca.id
        WHERE ca.project.id = :projectId
        """)
    List<Disbursement> findAllByProjectId(UUID projectId);
    
}

