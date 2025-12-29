package com.tnh.baseware.core.resources.investment.statistic;

import com.tnh.baseware.core.dtos.investment.statistic.OwnerStatisticDTO;
import com.tnh.baseware.core.services.investment.statistic.IOwnerStatisticService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/statistics/owners")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Owner Statistics", description = "APIs for Owner (Investor) Statistical Reports")
public class OwnerStatisticController {

    IOwnerStatisticService ownerStatisticService;

    @GetMapping
    @Operation(summary = "Get owner statistics by year and month")
    public ResponseEntity<List<OwnerStatisticDTO>> getOwnerStatistics(
            @RequestParam(required = true) Integer year,
            @RequestParam(required = true) Integer month) {
        return ResponseEntity.ok(ownerStatisticService.getOwnerStatistics(year, month));
    }
}
