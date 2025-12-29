package com.tnh.baseware.core.resources.investment.statistic;

import com.tnh.baseware.core.dtos.investment.statistic.CapitalStatisticDTO;
import com.tnh.baseware.core.services.investment.statistic.ICapitalStatisticService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/statistics/capitals")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Capital Statistics", description = "APIs for Capital Statistical Reports")
public class CapitalStatisticController {

    ICapitalStatisticService capitalStatisticService;

    @GetMapping
    @Operation(summary = "Get capital statistics by year and month")
    public ResponseEntity<List<CapitalStatisticDTO>> getCapitalStatistics(
            @RequestParam(required = true) Integer year,
            @RequestParam(required = true) Integer month) {
        return ResponseEntity.ok(capitalStatisticService.getCapitalStatistics(year, month));
    }
}
