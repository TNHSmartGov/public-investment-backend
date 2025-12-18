package com.tnh.baseware.core.repositories.user;

import com.tnh.baseware.core.entities.user.Role;
import com.tnh.baseware.core.repositories.IGenericRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IRoleRepository extends IGenericRepository<Role, UUID> {
    Role findByName(String name);
}
