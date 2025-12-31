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

    @GetMapping("/top-lowest-rate")
    @Operation(summary = "Get top owners with lowest disbursement rate")
    public ResponseEntity<List<OwnerStatisticDTO>> getTopLowestRateOwners(
            @RequestParam(required = true) Integer planYear,
            @RequestParam(required = false) Integer reportYear,
            @RequestParam(required = false) Integer reportMonth,
            @RequestParam(required = false, defaultValue = "10") Integer limit) {
        return ResponseEntity.ok(ownerStatisticService.getTopLowestRateOwners(planYear, reportYear, reportMonth, limit));
    }

    @GetMapping("/top-lowest-amount")
    @Operation(summary = "Get top owners with lowest disbursement amount")
    public ResponseEntity<List<OwnerStatisticDTO>> getTopLowestAmountOwners(
            @RequestParam(required = true) Integer planYear,
            @RequestParam(required = false) Integer reportYear,
            @RequestParam(required = false) Integer reportMonth,
            @RequestParam(required = false, defaultValue = "10") Integer limit) {
        return ResponseEntity.ok(ownerStatisticService.getTopLowestAmountOwners(planYear, reportYear, reportMonth, limit));
    }

    @GetMapping
    @Operation(summary = "Get owner statistics by year and month")
    public ResponseEntity<List<OwnerStatisticDTO>> getOwnerStatistics(
            @RequestParam(required = true) Integer planYear,
            @RequestParam(required = false) Integer reportYear,
            @RequestParam(required = false) Integer reportMonth) {
        return ResponseEntity.ok(ownerStatisticService.getOwnerStatistics(planYear, reportYear, reportMonth));
    }
}
