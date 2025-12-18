package com.tnh.baseware.core.repositories.investment;

import java.util.UUID;

import com.tnh.baseware.core.entities.investment.ApprovalDecision;
import com.tnh.baseware.core.repositories.IGenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IApprovalDecisionRepository extends IGenericRepository<ApprovalDecision, UUID> {

}
