package com.tnh.baseware.core.repositories.user;

import com.tnh.baseware.core.entities.user.Privilege;
import com.tnh.baseware.core.repositories.IGenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IPrivilegeRepository extends IGenericRepository<Privilege, UUID> {

    @Query("SELECT DISTINCT p.resourceName FROM Privilege p")
    List<String> findAllResourceNames();
}
