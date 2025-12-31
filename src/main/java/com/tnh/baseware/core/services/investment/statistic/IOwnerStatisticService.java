package com.tnh.baseware.core.services.investment.statistic;

import com.tnh.baseware.core.dtos.investment.statistic.OwnerStatisticDTO;
import java.util.List;

public interface IOwnerStatisticService {
    List<OwnerStatisticDTO> getOwnerStatistics(Integer planYear, Integer reportYear, Integer reportMonth);

    List<OwnerStatisticDTO> getTopLowestRateOwners(Integer planYear, Integer reportYear, Integer reportMonth, Integer limit);

    List<OwnerStatisticDTO> getTopLowestAmountOwners(Integer planYear, Integer reportYear, Integer reportMonth, Integer limit);
}
