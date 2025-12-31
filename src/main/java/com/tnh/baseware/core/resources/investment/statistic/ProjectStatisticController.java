package com.tnh.baseware.core.resources.investment.statistic;

import com.tnh.baseware.core.dtos.investment.statistic.ProjectStatisticDTO;
import com.tnh.baseware.core.services.investment.statistic.IProjectStatisticService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/statistics/projects")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Project Statistics", description = "APIs for Project Statistical Reports")
public class ProjectStatisticController {

    IProjectStatisticService projectStatisticService;

    @GetMapping
    @Operation(summary = "Get project statistics by year and month")
    public ResponseEntity<List<ProjectStatisticDTO>> getProjectStatistics(
            @RequestParam(required = true) Integer planYear,
            @RequestParam(required = false) Integer reportYear,
            @RequestParam(required = false) Integer reportMonth) {
        return ResponseEntity.ok(projectStatisticService.getProjectStatistics(planYear, reportYear, reportMonth));
    }
}
