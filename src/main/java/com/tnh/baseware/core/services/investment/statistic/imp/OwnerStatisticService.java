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
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OwnerStatisticService implements IOwnerStatisticService {

    IOrganizationRepository organizationRepository;
    ICapitalPlanLineRepository capitalPlanLineRepository;
    IAllocationExecutionRepository allocationExecutionRepository;
    IDisbursementRepository disbursementRepository;

    @Override
    public List<OwnerStatisticDTO> getOwnerStatistics(Integer year, Integer month) {
        List<OwnerStatisticDTO> result = new ArrayList<>();
        // In a real scenario, we might filtering organizations by type (e.g., only Investors)
        List<Organization> owners = organizationRepository.findAll();

        // Calculate end of the reporting month
        Instant reportDate = null;
        if (year != null && month != null) {
             YearMonth yearMonth = YearMonth.of(year, month);
             reportDate = yearMonth.atEndOfMonth().atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();
        }

        for (Organization owner : owners) {
            BigDecimal yearPlanAmount = BigDecimal.ZERO;
            BigDecimal executionAmount = BigDecimal.ZERO;
            BigDecimal disbursementAmount = BigDecimal.ZERO;

            if (year != null) {
                yearPlanAmount = capitalPlanLineRepository.sumAmountByOwnerIdAndYear(owner.getId(), year);
                if (yearPlanAmount == null) yearPlanAmount = BigDecimal.ZERO;

                if (reportDate != null) {
                    executionAmount = allocationExecutionRepository.sumAmountByOwnerIdAndYearAndDateBefore(
                            owner.getId(), year, reportDate);
                    disbursementAmount = disbursementRepository.sumAmountByOwnerIdAndYearAndDateBefore(
                            owner.getId(), year, reportDate);
                }
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
}
