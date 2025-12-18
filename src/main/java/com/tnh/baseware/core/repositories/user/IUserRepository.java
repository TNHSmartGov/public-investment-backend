package com.tnh.baseware.core.repositories.user;

import com.tnh.baseware.core.entities.user.User;
import com.tnh.baseware.core.repositories.IGenericRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository extends IGenericRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    boolean existsByUsernameOrPhoneOrEmailOrIdn(String username, String phone, String email, String idn);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsByIdn(String idn);

}
