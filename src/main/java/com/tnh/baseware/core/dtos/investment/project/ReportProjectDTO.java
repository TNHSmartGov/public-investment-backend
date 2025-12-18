package com.tnh.baseware.core.dtos.investment.project;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

import com.tnh.baseware.core.dtos.investment.capital.ReportCapitalAllocationDTO;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportProjectDTO {

    UUID id;
    String name;
    List<ReportCapitalAllocationDTO> capitalAllocations;

}
