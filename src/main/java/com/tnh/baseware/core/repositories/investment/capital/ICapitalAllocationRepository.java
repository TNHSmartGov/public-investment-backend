package com.tnh.baseware.core.repositories.investment.capital;

import java.util.UUID;
import java.util.List;

import com.tnh.baseware.core.entities.investment.capital.CapitalAllocation;
import com.tnh.baseware.core.repositories.IGenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICapitalAllocationRepository extends IGenericRepository<CapitalAllocation, UUID> {

    void deleteAllByProject_Id(UUID projectId);
    List<CapitalAllocation> findByProjectId(UUID projectId);
}