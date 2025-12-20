package com.tnh.baseware.core.repositories.investment.capital;

import com.tnh.baseware.core.entities.investment.capital.ProjectCapitalAllocation;
import com.tnh.baseware.core.repositories.IGenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface IProjectCapitalAllocationRepository extends IGenericRepository<ProjectCapitalAllocation, UUID> {

    /**
     * Tính tổng vốn trung hạn đã giao cho tất cả các dự án từ một nguồn vốn cụ thể.
     * Dùng trong trường hợp CREATE.
     */
    @Query("SELECT SUM(p.amountInMediumTerm) FROM ProjectCapitalAllocation p " +
           "WHERE p.capitalPlan.id = :capitalPlanId AND p.deleted = false")
    BigDecimal sumAmountByCapitalPlanId(@Param("capitalPlanId") UUID capitalPlanId);

    /**
     * Tính tổng vốn trung hạn đã giao, loại trừ bản ghi đang cập nhật.
     * Dùng trong trường hợp UPDATE để tránh cộng trùng số tiền cũ.
     */
    @Query("SELECT SUM(p.amountInMediumTerm) FROM ProjectCapitalAllocation p " +
           "WHERE p.capitalPlan.id = :capitalPlanId AND p.id <> :excludeId AND p.deleted = false")
    BigDecimal sumAmountByCapitalPlanIdAndExcludeId(@Param("capitalPlanId") UUID capitalPlanId, 
                                                    @Param("excludeId") UUID excludeId);

    /**
     * Kiểm tra xem một dự án đã được giao vốn từ nguồn này chưa (Tránh trùng lặp)
     */
    boolean existsByProjectIdAndCapitalPlanIdAndDeletedFalse(UUID projectId, UUID capitalPlanId);
}