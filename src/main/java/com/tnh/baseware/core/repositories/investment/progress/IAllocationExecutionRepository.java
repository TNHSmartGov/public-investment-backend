package com.tnh.baseware.core.repositories.investment.progress;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tnh.baseware.core.entities.investment.progress.AllocationExecution;
import com.tnh.baseware.core.repositories.IGenericRepository;

@Repository
public interface IAllocationExecutionRepository extends IGenericRepository<AllocationExecution, UUID> {

    @Query(value = """
        SELECT aex
        FROM AllocationExecution aex
        JOIN CapitalAllocationDetail cad ON aex.capitalAllocationDetail.id = cad.id
        JOIN CapitalAllocation ca ON cad.capitalAllocation.id = ca.id
        WHERE ca.project.id = :projectId
        """)
    List<AllocationExecution> findAllByProjectId(UUID projectId);
}
