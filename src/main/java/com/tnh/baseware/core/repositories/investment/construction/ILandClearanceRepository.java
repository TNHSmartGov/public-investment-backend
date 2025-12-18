package com.tnh.baseware.core.repositories.investment.construction;

import java.util.UUID;

import com.tnh.baseware.core.entities.investment.construction.LandClearance;
import com.tnh.baseware.core.repositories.IGenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILandClearanceRepository  extends IGenericRepository<LandClearance, UUID> {

}
