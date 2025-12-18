package com.tnh.baseware.core.repositories.investment.capital;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.tnh.baseware.core.entities.investment.capital.CapitalPlan;
import com.tnh.baseware.core.repositories.IGenericRepository;

@Repository
public interface ICapitalPlanRepository extends IGenericRepository<CapitalPlan, UUID> {

}
