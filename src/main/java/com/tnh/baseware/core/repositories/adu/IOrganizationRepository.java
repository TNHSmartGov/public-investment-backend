package com.tnh.baseware.core.repositories.adu;

import com.tnh.baseware.core.entities.adu.Organization;
import com.tnh.baseware.core.repositories.IGenericRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IOrganizationRepository extends IGenericRepository<Organization, UUID> {

    @EntityGraph(attributePaths = {"parent"})
    @Query("SELECT o FROM Organization o")
    List<Organization> findAllWithParent();

    @Query("SELECT o FROM Organization o JOIN o.users u WHERE u.id = :userId AND o.level=4")
    List<Organization> findAllOwnersByUser(UUID userId);
}
