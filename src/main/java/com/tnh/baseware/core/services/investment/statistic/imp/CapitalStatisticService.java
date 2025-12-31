package com.tnh.baseware.core.services.investment.statistic.imp;

import com.tnh.baseware.core.dtos.investment.statistic.CapitalStatisticDTO;
import com.tnh.baseware.core.entities.investment.capital.Capital;
import com.tnh.baseware.core.repositories.investment.capital.ICapitalRepository;
import com.tnh.baseware.core.repositories.investment.capital.ICapitalPlanLineRepository;
import com.tnh.baseware.core.repositories.investment.progress.IAllocationExecutionRepository;
import com.tnh.baseware.core.repositories.investment.progress.IDisbursementRepository;
import com.tnh.baseware.core.services.investment.statistic.ICapitalStatisticService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CapitalStatisticService implements ICapitalStatisticService {

    ICapitalRepository capitalRepository;
    ICapitalPlanLineRepository capitalPlanLineRepository;
    IAllocationExecutionRepository allocationExecutionRepository;
    IDisbursementRepository disbursementRepository;

    @Override
    public List<CapitalStatisticDTO> getCapitalStatistics(Integer planYear, Integer reportYear, Integer reportMonth) {
        List<CapitalStatisticDTO> result = new ArrayList<>();
        List<Capital> capitals = capitalRepository.findAll();

        // Calculate end of the reporting period (cutoff date)
        Instant reportDate = null;
        if (reportYear != null) {
            int month = (reportMonth != null) ? reportMonth : 12; // Default to end of year if month is missing
            YearMonth yearMonth = YearMonth.of(reportYear, month);
            reportDate = yearMonth.atEndOfMonth().atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();
        }

        for (Capital capital : capitals) {
            BigDecimal yearPlanAmount = BigDecimal.ZERO;
            BigDecimal executionAmount = BigDecimal.ZERO;
            BigDecimal disbursementAmount = BigDecimal.ZERO;

            if (planYear != null) {
                // Kế hoạch vốn: Lấy theo Năm kế hoạch (planYear)
                yearPlanAmount = capitalPlanLineRepository.sumAmountByCapitalIdAndYear(capital.getId(), planYear);
                if (yearPlanAmount == null) yearPlanAmount = BigDecimal.ZERO;

                // Thực hiện & Giải ngân: Lấy theo Năm kế hoạch + Tích lũy đến thời điểm báo cáo (reportDate)
                executionAmount = allocationExecutionRepository.sumAmountByCapitalIdAndYearAndDateBefore(
                        capital.getId(), planYear, reportDate);
                disbursementAmount = disbursementRepository.sumAmountByCapitalIdAndYearAndDateBefore(
                        capital.getId(), planYear, reportDate);
            }

            if (executionAmount == null) executionAmount = BigDecimal.ZERO;
            if (disbursementAmount == null) disbursementAmount = BigDecimal.ZERO;

            result.add(CapitalStatisticDTO.builder()
                    .id(capital.getId())
                    .code(capital.getCode())
                    .name(capital.getName())
                    .yearPlanAmount(yearPlanAmount)
                    .executionAmount(executionAmount)
                    .disbursementAmount(disbursementAmount)
                    .build());
        }

        return result;
    }
}
