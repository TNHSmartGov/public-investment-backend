package com.tnh.baseware.core.repositories.investment;

import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tnh.baseware.core.entities.adu.Organization;
import com.tnh.baseware.core.entities.investment.Project;
import com.tnh.baseware.core.repositories.IGenericRepository;
import com.tnh.baseware.core.dtos.investment.OwnerDTO;

@Repository
public interface IProjectRepository extends IGenericRepository<Project, UUID> {
    
}
