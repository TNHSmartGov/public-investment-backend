package com.tnh.baseware.core.services.investment.statistic;

import com.tnh.baseware.core.dtos.investment.statistic.CapitalStatisticDTO;
import java.util.List;

public interface ICapitalStatisticService {
    List<CapitalStatisticDTO> getCapitalStatistics(Integer year, Integer month);
}
