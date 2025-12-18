package com.tnh.baseware.core.repositories.user;

import com.tnh.baseware.core.entities.user.Menu;
import com.tnh.baseware.core.repositories.IGenericRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IMenuRepository extends IGenericRepository<Menu, UUID> {

    @EntityGraph(attributePaths = { "parent" })
    @Query("SELECT m FROM Menu m WHERE m.parent IS NULL order by m.menuOrder ASC ")
    List<Menu> findAllWithParent();
}
