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
    public List<CapitalStatisticDTO> getCapitalStatistics(Integer year, Integer month) {
        List<CapitalStatisticDTO> result = new ArrayList<>();
        List<Capital> capitals = capitalRepository.findAll();

        // Calculate end of the reporting month
        Instant reportDate = null;
        if (year != null && month != null) {
             YearMonth yearMonth = YearMonth.of(year, month);
             reportDate = yearMonth.atEndOfMonth().atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();
        }

        for (Capital capital : capitals) {
            BigDecimal yearPlanAmount = BigDecimal.ZERO;
            BigDecimal executionAmount = BigDecimal.ZERO;
            BigDecimal disbursementAmount = BigDecimal.ZERO;

            if (year != null) {
                yearPlanAmount = capitalPlanLineRepository.sumAmountByCapitalIdAndYear(capital.getId(), year);
                if (yearPlanAmount == null) yearPlanAmount = BigDecimal.ZERO;

                if (reportDate != null) {
                    executionAmount = allocationExecutionRepository.sumAmountByCapitalIdAndYearAndDateBefore(
                            capital.getId(), year, reportDate);
                    disbursementAmount = disbursementRepository.sumAmountByCapitalIdAndYearAndDateBefore(
                            capital.getId(), year, reportDate);
                }
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
