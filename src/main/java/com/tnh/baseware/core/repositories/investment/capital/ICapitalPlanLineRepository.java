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

    // 2. Tính tổng số tiền phân bổ, loại trừ bản ghi hiện tại (dùng cho Update)
    @Query("SELECT SUM(c.amount) FROM CapitalPlanLine c " +
           "WHERE c.capitalPlan.id = :capitalPlanId AND c.id <> :excludeId AND c.deleted = false")
    BigDecimal sumAmountByCapitalPlanIdAndExcludeId(@Param("capitalPlanId") UUID capitalPlanId, 
                                                    @Param("excludeId") UUID excludeId);

    // 3. Tìm tất cả dòng kế hoạch chưa xóa (nếu GenericRepository chưa có)
    List<CapitalPlanLine> findAllByDeletedFalse();

    // 4. (Tùy chọn) Kiểm tra xem năm đó đã tồn tại kế hoạch cho nguồn này chưa
    Boolean existsByCapitalPlanIdAndYearAndDeletedFalse(UUID capitalPlanId, Integer year);
}