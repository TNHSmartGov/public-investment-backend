package com.tnh.baseware.core.repositories.investment.capital;

import com.tnh.baseware.core.entities.investment.capital.Capital;
import com.tnh.baseware.core.repositories.IGenericRepository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ICapitalRepository extends IGenericRepository<Capital, UUID> {
   
    @EntityGraph(attributePaths = {"parent"})
    @Query("SELECT m FROM Capital m")
    List<Capital> findAllWithParent();
}

