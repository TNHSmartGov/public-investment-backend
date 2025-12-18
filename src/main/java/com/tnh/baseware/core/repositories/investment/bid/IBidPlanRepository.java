package com.tnh.baseware.core.repositories.investment.bid;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.tnh.baseware.core.entities.investment.bid.BidPlan;
import com.tnh.baseware.core.repositories.IGenericRepository;

@Repository
public interface IBidPlanRepository extends IGenericRepository<BidPlan, UUID> {

}
