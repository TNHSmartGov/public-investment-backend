package com.tnh.baseware.core.repositories.investment.progress;

import com.tnh.baseware.core.entities.investment.progress.AllocationExecution;
import com.tnh.baseware.core.repositories.IGenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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

    List<AllocationExecution> findAllByProjectIdAndDeletedFalse(UUID projectId);
}