package com.tnh.baseware.core.repositories.investment.capital;

import com.tnh.baseware.core.entities.investment.capital.CapitalPlanLine;
import com.tnh.baseware.core.repositories.IGenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface ICapitalPlanLineRepository extends IGenericRepository<CapitalPlanLine, UUID> {

    // 1. Tính tổng số tiền đã phân bổ cho một kế hoạch trung hạn (dùng cho Create)
    @Query("SELECT SUM(c.amount) FROM CapitalPlanLine c " +
           "WHERE c.capitalPlan.id = :capitalPlanId AND c.deleted = false")
    BigDecimal sumAmountByCapitalPlanId(@Param("capitalPlanId") UUID capitalPlanId);

    // New query: Tính tổng phân bổ cho một dự án trong một năm cụ thể
    @Query("SELECT SUM(cpl.amount) FROM CapitalPlanLine cpl " +
           "WHERE cpl.projectCapitalAllocation.project.id = :projectId " +
           "AND cpl.year = :year")
    BigDecimal sumAmountByProjectIdAndYear(@Param("projectId") UUID projectId, @Param("year") Integer year);

    // New query: Tính tổng kế hoạch vốn của một nguồn vốn trong một năm
    @Query("SELECT SUM(cpl.amount) FROM CapitalPlanLine cpl " +
           "WHERE cpl.capitalPlan.capital.id = :capitalId " +
           "AND cpl.year = :year")
    BigDecimal sumAmountByCapitalIdAndYear(@Param("capitalId") UUID capitalId, @Param("year") Integer year);

    // New query: Tính tổng kế hoạch vốn của một chủ đầu tư trong một năm
    @Query("SELECT SUM(cpl.amount) FROM CapitalPlanLine cpl " +
           "WHERE cpl.projectCapitalAllocation.project.ownerOrg.id = :ownerId " +
           "AND cpl.year = :year")
    BigDecimal sumAmountByOwnerIdAndYear(@Param("ownerId") UUID ownerId, @Param("year") Integer year);

    // 2. Tính tổng số tiền phân bổ, loại trừ bản ghi hiện tại (dùng cho Update)
    @Query("SELECT SUM(c.amount) FROM CapitalPlanLine c " +
           "WHERE c.capitalPlan.id = :capitalPlanId AND c.id <> :excludeId AND c.deleted = false")
    BigDecimal sumAmountByCapitalPlanIdAndExcludeId(@Param("capitalPlanId") UUID capitalPlanId, 
                                                    @Param("excludeId") UUID excludeId);

    // 3. Tìm tất cả dòng kế hoạch chưa xóa (nếu GenericRepository chưa có)
    List<CapitalPlanLine> findAllByDeletedFalse();

    // 4. (Tùy chọn) Kiểm tra xem năm đó đã tồn tại kế hoạch cho nguồn này chưa
    // 4. Tính tổng phân bổ cho một dự án trong nguồn vốn này (Validation Logic mới)
    @Query("SELECT SUM(c.amount) FROM CapitalPlanLine c " +
           "WHERE c.projectCapitalAllocation.id = :projectAllocationId AND c.deleted = false")
    BigDecimal sumAmountByProjectAllocationId(@Param("projectAllocationId") UUID projectAllocationId);

    // 5. Tính tổng phân bổ, loại trừ dòng hiện tại (Update validation)
    @Query("SELECT SUM(c.amount) FROM CapitalPlanLine c " +
           "WHERE c.projectCapitalAllocation.id = :projectAllocationId AND c.id <> :excludeId AND c.deleted = false")
    BigDecimal sumAmountByProjectAllocationIdAndExcludeId(@Param("projectAllocationId") UUID projectAllocationId, 
                                                          @Param("excludeId") UUID excludeId);

    Boolean existsByCapitalPlanIdAndYearAndDeletedFalse(UUID capitalPlanId, Integer year);
}