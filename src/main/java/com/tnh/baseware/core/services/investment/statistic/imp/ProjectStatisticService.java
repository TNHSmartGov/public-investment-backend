package com.tnh.baseware.core.services.investment.statistic.imp;

import com.tnh.baseware.core.dtos.investment.statistic.ProjectStatisticDTO;
import com.tnh.baseware.core.entities.investment.Project;
import com.tnh.baseware.core.repositories.investment.IProjectRepository;
import com.tnh.baseware.core.repositories.investment.capital.ICapitalPlanLineRepository;
import com.tnh.baseware.core.repositories.investment.progress.IAllocationExecutionRepository;
import com.tnh.baseware.core.repositories.investment.progress.IDisbursementRepository;
import com.tnh.baseware.core.services.investment.statistic.IProjectStatisticService;
import com.tnh.baseware.core.utils.BasewareUtils;

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
public class ProjectStatisticService implements IProjectStatisticService {

    IProjectRepository projectRepository;
    ICapitalPlanLineRepository capitalPlanLineRepository;
    IAllocationExecutionRepository allocationExecutionRepository;
    IDisbursementRepository disbursementRepository;

    @Override
    public List<ProjectStatisticDTO> getProjectStatistics(Integer year, Integer month) {
        List<ProjectStatisticDTO> result = new ArrayList<>();
        List<Project> projects = projectRepository.findAll();

        // Calculate end of the reporting month
        Instant reportDate = null;
        if (year != null && month != null) {
             YearMonth yearMonth = YearMonth.of(year, month);
             reportDate = yearMonth.atEndOfMonth().atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();
        }

        for (Project project : projects) {
            BigDecimal yearPlanAmount = BigDecimal.ZERO;
            BigDecimal executionAmount = BigDecimal.ZERO;
            BigDecimal disbursementAmount = BigDecimal.ZERO;

            if (year != null) {
                yearPlanAmount = capitalPlanLineRepository.sumAmountByProjectIdAndYear(project.getId(), year);
                if (yearPlanAmount == null) yearPlanAmount = BigDecimal.ZERO;

                if (reportDate != null) {
                    executionAmount = allocationExecutionRepository.sumAmountByProjectIdAndYearAndDateBefore(
                            project.getId(), year, reportDate);
                    disbursementAmount = disbursementRepository.sumAmountByProjectIdAndYearAndDateBefore(
                            project.getId(), year, reportDate);
                }
            }

            if (executionAmount == null) executionAmount = BigDecimal.ZERO;
            if (disbursementAmount == null) disbursementAmount = BigDecimal.ZERO;

            result.add(ProjectStatisticDTO.builder()
                    .id(project.getId())
                    .code(project.getCode())
                    .name(project.getName())
                    .totalInvestmentPlanned(project.getTotalInvestmentPlanned())
                    .yearPlanAmount(yearPlanAmount)
                    .executionAmount(executionAmount)
                    .disbursementAmount(disbursementAmount)
                    .build());
        }

        return result;
    }
}
