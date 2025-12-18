package com.tnh.baseware.core.repositories.investment;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tnh.baseware.core.entities.investment.Agency;
import com.tnh.baseware.core.repositories.IGenericRepository;

@Repository
public interface IAgencyRepository extends IGenericRepository<Agency, UUID> {

    @EntityGraph(attributePaths = {"parent"})
    @Query("SELECT m FROM Agency m")
    List<Agency> findAllWithParent();
}
