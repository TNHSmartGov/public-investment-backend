package com.tnh.baseware.core.services.investment.statistic.imp;

import com.tnh.baseware.core.dtos.investment.statistic.OwnerStatisticDTO;
import com.tnh.baseware.core.entities.adu.Organization;
import com.tnh.baseware.core.repositories.adu.IOrganizationRepository;
import com.tnh.baseware.core.repositories.investment.capital.ICapitalPlanLineRepository;
import com.tnh.baseware.core.repositories.investment.progress.IAllocationExecutionRepository;
import com.tnh.baseware.core.repositories.investment.progress.IDisbursementRepository;
import com.tnh.baseware.core.services.investment.statistic.IOwnerStatisticService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OwnerStatisticService implements IOwnerStatisticService {

    IOrganizationRepository organizationRepository;
    ICapitalPlanLineRepository capitalPlanLineRepository;
    IAllocationExecutionRepository allocationExecutionRepository;
    IDisbursementRepository disbursementRepository;

    @Override
    public List<OwnerStatisticDTO> getOwnerStatistics(Integer planYear, Integer reportYear, Integer reportMonth) {
        List<OwnerStatisticDTO> result = new ArrayList<>();
        // In a real scenario, we might filtering organizations by type (e.g., only Investors)
        List<Organization> owners = organizationRepository.findAll();

        // Calculate end of the reporting period (cutoff date)
        Instant reportDate = null;
        if (reportYear != null) {
            int month = (reportMonth != null) ? reportMonth : 12; // Default to end of year if month is missing
            YearMonth yearMonth = YearMonth.of(reportYear, month);
            reportDate = yearMonth.atEndOfMonth().atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();
        }

        for (Organization owner : owners) {
            BigDecimal yearPlanAmount = BigDecimal.ZERO;
            BigDecimal executionAmount = BigDecimal.ZERO;
            BigDecimal disbursementAmount = BigDecimal.ZERO;

            if (planYear != null) {
                yearPlanAmount = capitalPlanLineRepository.sumAmountByOwnerIdAndYear(owner.getId(), planYear);
                if (yearPlanAmount == null) yearPlanAmount = BigDecimal.ZERO;

                // Thực hiện & Giải ngân: Lấy theo Năm kế hoạch + Tích lũy đến thời điểm báo cáo (reportDate)
                executionAmount = allocationExecutionRepository.sumAmountByOwnerIdAndYearAndDateBefore(
                        owner.getId(), planYear, reportDate);
                disbursementAmount = disbursementRepository.sumAmountByOwnerIdAndYearAndDateBefore(
                        owner.getId(), planYear, reportDate);
            }
            
            // Only add to result if there is relevant data to show, or return all if needed.
            // Returning all to be consistent.
            if (executionAmount == null) executionAmount = BigDecimal.ZERO;
            if (disbursementAmount == null) disbursementAmount = BigDecimal.ZERO;

            result.add(OwnerStatisticDTO.builder()
                    .id(owner.getId())
                    .code(owner.getCode())
                    .name(owner.getName())
                    .yearPlanAmount(yearPlanAmount)
                    .executionAmount(executionAmount)
                    .disbursementAmount(disbursementAmount)
                    .build());
        }

        return result;
    }
    @Override
    public List<OwnerStatisticDTO> getTopLowestRateOwners(Integer planYear, Integer reportYear, Integer reportMonth, Integer limit) {
        if (limit == null) limit = 10;

        // 1. Lấy danh sách thống kê đầy đủ
        List<OwnerStatisticDTO> allStats = getOwnerStatistics(planYear, reportYear, reportMonth);

        // 2. Lọc & Tính toán tỷ lệ
        allStats.forEach(dto -> {
            BigDecimal plan = dto.getYearPlanAmount();
            BigDecimal disburse = dto.getDisbursementAmount();

            if (plan != null && plan.compareTo(BigDecimal.ZERO) > 0) {
                // Rate = (Disbursement / Plan) * 100
                double rate = disburse.doubleValue() / plan.doubleValue() * 100;
                dto.setDisbursementRate(rate);
            } else {
                // Kế hoạch = 0 -> Tỷ lệ = 0 (hoặc bỏ qua tùy nghiệp vụ, ở đây set 0 để đẩy xuống dưới nếu sort, hoặc filter out)
                // Theo yêu cầu: "có tỷ lệ giải ngân thấp" -> thường là có giao vốn nhưng giải ngân ít.
                // Nếu vốn = 0 thì không tính là chậm giải ngân. -> Set rate = -1 hoặc filter.
                // Quyết định: Set null để filter out.
                dto.setDisbursementRate(null);
            }
        });

        // 3. Filter & Sort & Limit
        return allStats.stream()
                .filter(dto -> dto.getDisbursementRate() != null) // Chỉ lấy người có kế hoạch vốn > 0
                .sorted(Comparator.comparingDouble(OwnerStatisticDTO::getDisbursementRate)) // Thấp đến cao
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<OwnerStatisticDTO> getTopLowestAmountOwners(Integer planYear, Integer reportYear, Integer reportMonth, Integer limit) {
        if (limit == null) limit = 10;

        // 1. Lấy danh sách thống kê đầy đủ
        List<OwnerStatisticDTO> allStats = getOwnerStatistics(planYear, reportYear, reportMonth);

        // 2. Filter & Sort & Limit
        return allStats.stream()
                // Chỉ lấy người có kế hoạch vốn > 0. Nếu vốn = 0 thì không gọi là giải ngân thấp (vì không có gì để giải ngân).
                .filter(dto -> dto.getYearPlanAmount() != null && dto.getYearPlanAmount().compareTo(BigDecimal.ZERO) > 0)
                .sorted(Comparator.comparing(OwnerStatisticDTO::getDisbursementAmount)) // Thấp đến cao (Amount)
                .limit(limit)
                .collect(Collectors.toList());
    }
}
