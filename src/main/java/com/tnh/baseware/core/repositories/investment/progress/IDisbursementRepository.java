package com.tnh.baseware.core.repositories.investment.progress;

import com.tnh.baseware.core.entities.investment.progress.Disbursement;
import com.tnh.baseware.core.repositories.IGenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.time.Instant;
import java.util.UUID;

@Repository
public interface IDisbursementRepository extends IGenericRepository<Disbursement, UUID> {

    /**
     * Tính tổng số tiền đã giải ngân thực tế cho một dòng kế hoạch năm cụ thể.
     * Sử dụng khi CREATE bản ghi mới.
     */
    @Query("SELECT SUM(d.amount) FROM Disbursement d " +
           "WHERE d.capitalPlanLine.id = :lineId AND d.deleted = false")
    BigDecimal sumAmountByCapitalPlanLineId(@Param("lineId") UUID lineId);

    /**
     * Tính tổng số tiền đã giải ngân thực tế, loại trừ bản ghi đang cập nhật.
     * Sử dụng khi UPDATE để tránh tính trùng số tiền cũ của chính nó.
     */
    @Query("SELECT SUM(d.amount) FROM Disbursement d " +
           "WHERE d.capitalPlanLine.id = :lineId AND d.id <> :excludeId AND d.deleted = false")
    BigDecimal sumAmountByCapitalPlanLineIdAndExcludeId(@Param("lineId") UUID lineId, 
                                                        @Param("excludeId") UUID excludeId);

    @Query("SELECT SUM(ae.amount) FROM Disbursement ae " +
           "WHERE ae.project.id = :projectId " +
           "AND ae.capitalPlanLine.year = :year " +
           "AND (:reportDate IS NULL OR ae.disbursementDate <= :reportDate)")
    BigDecimal sumAmountByProjectIdAndYearAndDateBefore(@Param("projectId") UUID projectId,
                                                        @Param("year") Integer year,
                                                        @Param("reportDate") Instant reportDate);

    // New query: Tính tổng giải ngân của một nguồn vốn trong một năm tích lũy đến ngày báo cáo
    @Query("SELECT SUM(d.amount) FROM Disbursement d " +
           "WHERE d.capitalPlanLine.capitalPlan.capital.id = :capitalId " +
           "AND d.capitalPlanLine.year = :year " +
           "AND (:reportDate IS NULL OR d.disbursementDate <= :reportDate)")
    BigDecimal sumAmountByCapitalIdAndYearAndDateBefore(@Param("capitalId") UUID capitalId,
                                                        @Param("year") Integer year,
                                                        @Param("reportDate") Instant reportDate);

    // New query: Tính tổng giải ngân của một chủ đầu tư trong một năm tích lũy đến ngày báo cáo
    @Query("SELECT SUM(d.amount) FROM Disbursement d " +
           "WHERE d.project.ownerOrg.id = :ownerId " +
           "AND d.capitalPlanLine.year = :year " +
           "AND (:reportDate IS NULL OR d.disbursementDate <= :reportDate)")
    BigDecimal sumAmountByOwnerIdAndYearAndDateBefore(@Param("ownerId") UUID ownerId,
                                                      @Param("year") Integer year,
                                                      @Param("reportDate") Instant reportDate);

    List<Disbursement> findAllByProjectIdAndDeletedFalse(UUID projectId);                                                    
}