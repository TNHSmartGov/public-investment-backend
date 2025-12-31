package com.tnh.baseware.core.repositories.investment.progress;

import com.tnh.baseware.core.entities.investment.progress.AllocationExecution;
import com.tnh.baseware.core.repositories.IGenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import java.util.List;

@Repository
public interface IAllocationExecutionRepository extends IGenericRepository<AllocationExecution, UUID> {

    /**
     * Tính tổng lũy kế số tiền đã thực hiện cho một dòng kế hoạch năm.
     * Dùng để kiểm tra hạn mức khi tạo mới bản ghi thực hiện (Create).
     */
    @Query("SELECT SUM(a.amount) FROM AllocationExecution a " +
           "WHERE a.capitalPlanLine.id = :lineId AND a.deleted = false")
    BigDecimal sumAmountByCapitalPlanLineId(@Param("lineId") UUID lineId);

    /**
     * Tính tổng lũy kế số tiền đã thực hiện, loại trừ bản ghi đang cập nhật.
     * Dùng để kiểm tra hạn mức khi chỉnh sửa (Update).
     */
    @Query("SELECT SUM(a.amount) FROM AllocationExecution a " +
           "WHERE a.capitalPlanLine.id = :lineId AND a.id <> :excludeId AND a.deleted = false")
    BigDecimal sumAmountByCapitalPlanLineIdAndExcludeId(@Param("lineId") UUID lineId, 
                                                        @Param("excludeId") UUID excludeId);

    /**
     * Tính tổng lũy kế số tiền đã thực hiện cho một dự án, trong một năm cụ thể và đến một ngày báo cáo nhất định.
     * Dùng để kiểm tra hạn mức khi tạo mới bản ghi thực hiện (Create).
     */
    @Query("SELECT SUM(ae.amount) FROM AllocationExecution ae " +
           "WHERE ae.project.id = :projectId " +
           "AND ae.capitalPlanLine.year = :year " +
           "AND (:reportDate IS NULL OR ae.executionDate <= :reportDate)")
    BigDecimal sumAmountByProjectIdAndYearAndDateBefore(@Param("projectId") UUID projectId,
                                                        @Param("year") Integer year,
                                                        @Param("reportDate") Instant reportDate);

    // New query: Tính tổng giá trị thực hiện của một nguồn vốn trong một năm tích lũy đến ngày báo cáo
    @Query("SELECT SUM(ae.amount) FROM AllocationExecution ae " +
           "WHERE ae.capitalPlanLine.capitalPlan.capital.id = :capitalId " +
           "AND ae.capitalPlanLine.year = :year " +
           "AND (:reportDate IS NULL OR ae.executionDate <= :reportDate)")
    BigDecimal sumAmountByCapitalIdAndYearAndDateBefore(@Param("capitalId") UUID capitalId,
                                                        @Param("year") Integer year,
                                                        @Param("reportDate") Instant reportDate);

    // New query: Tính tổng giá trị thực hiện của một chủ đầu tư trong một năm tích lũy đến ngày báo cáo
    @Query("SELECT SUM(ae.amount) FROM AllocationExecution ae " +
           "WHERE ae.project.ownerOrg.id = :ownerId " +
           "AND ae.capitalPlanLine.year = :year " +
           "AND (:reportDate IS NULL OR ae.executionDate <= :reportDate)")
    BigDecimal sumAmountByOwnerIdAndYearAndDateBefore(@Param("ownerId") UUID ownerId,
                                                      @Param("year") Integer year,
                                                      @Param("reportDate") Instant reportDate);

    List<AllocationExecution> findAllByProjectIdAndDeletedFalse(UUID projectId);
}