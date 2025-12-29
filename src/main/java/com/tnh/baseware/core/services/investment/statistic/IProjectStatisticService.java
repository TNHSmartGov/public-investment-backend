package com.tnh.baseware.core.services.investment.statistic;

import com.tnh.baseware.core.dtos.investment.statistic.ProjectStatisticDTO;
import java.util.List;

public interface IProjectStatisticService {
    List<ProjectStatisticDTO> getProjectStatistics(Integer year, Integer month);
}
