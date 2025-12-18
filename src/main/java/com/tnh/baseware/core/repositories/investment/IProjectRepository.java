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

    List<Project> findByOwnerOrgId(UUID ownerOrgId);
    
    @Query("""
        SELECT DISTINCT new com.tnh.baseware.core.dtos.investment.OwnerDTO(p.ownerOrg.id, p.ownerOrg.name)
        FROM Project p
        JOIN p.capitalAllocations ca
        JOIN ca.capitalAllocationDetails cad
        WHERE EXTRACT(YEAR FROM cad.allocationDate) = :year
    """)
    List<OwnerDTO> findOwnerOrgIdsWithCapitalAllocationInYear(Integer year);

    @Query("SELECT p FROM Project p WHERE p.ownerOrg.id IN :ownerIds")
    List<Project> getListProjectByOwners(List<UUID> ownerIds);

    Page<Project> findByOwnerOrgIn(List<Organization> ownerOrgs, Pageable pageable);

    List<Project> findByOwnerOrgIn(List<Organization> ownerOrgs);
    
}
