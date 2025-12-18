package com.tnh.baseware.core.repositories.investment;

import com.tnh.baseware.core.entities.investment.Industry;
import com.tnh.baseware.core.repositories.IGenericRepository;

import java.util.UUID;

import org.springframework.stereotype.Repository;
@Repository
public interface IIndustryRepository extends IGenericRepository<Industry, UUID> {
}
