package com.tnh.baseware.core.services.investment.statistic;

import com.tnh.baseware.core.dtos.investment.statistic.OwnerStatisticDTO;
import java.util.List;

public interface IOwnerStatisticService {
    List<OwnerStatisticDTO> getOwnerStatistics(Integer year, Integer month);
}
