package com.tnh.baseware.core.resources.investment.capital;

import com.tnh.baseware.core.dtos.user.ApiMessageDTO;
import com.tnh.baseware.core.entities.investment.capital.CapitalAllocation;
import com.tnh.baseware.core.dtos.investment.capital.CapitalAllocationDTO;
import com.tnh.baseware.core.forms.investment.CapitalAllocationEditorForm;
import com.tnh.baseware.core.properties.SystemProperties;
import com.tnh.baseware.core.resources.GenericResource;
import com.tnh.baseware.core.services.IGenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.investment.capital.ICapitalAllocationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "capital-allocations", description = "API for managing CapitalAllocations")
@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${baseware.core.system.api-prefix}/capital-allocations")
public class CapitalAllocationResource extends
        GenericResource<CapitalAllocation, CapitalAllocationEditorForm, CapitalAllocationDTO, UUID> {

    ICapitalAllocationService capitalAllocationService;

    public CapitalAllocationResource(IGenericService<CapitalAllocation, CapitalAllocationEditorForm, CapitalAllocationDTO, UUID> service,
            MessageService messageService, ICapitalAllocationService capitalAllocationtService,
            SystemProperties systemProperties) {
        super(service, messageService, systemProperties.getApiPrefix() + "/capital-allocations");
        this.capitalAllocationService = capitalAllocationtService;
    } 

    @Operation(summary = "Create a multiple entity")
    @ApiResponse(responseCode = "200", description = "Create Multiple Entity created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
    @PostMapping("/create-multiple")
    public ResponseEntity<ApiMessageDTO<List<CapitalAllocationDTO>>> createMany(@RequestBody List<CapitalAllocationEditorForm> forms) {
        var createdList = capitalAllocationService.createMany(forms);

        return ResponseEntity.ok(
            ApiMessageDTO.<List<CapitalAllocationDTO>>builder()
                .data(createdList)
                .result(true)
                .message("capital.allocation.created")
                .code(HttpStatus.CREATED.value())
                .build()
        );
    }

    @Operation(summary = "Approve a Capital Allocation entity")
    @ApiResponse(responseCode = "200", description = "Capital Allocation Entity Approved", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
    @PutMapping("/approve-capital-allocation/{id}")
    public ResponseEntity<ApiMessageDTO<Integer>> approveCapitalAllocation(@PathVariable UUID id) {
        
        capitalAllocationService.approve(id);

        return ResponseEntity.ok(ApiMessageDTO.<Integer>builder()
                                .data(1)
                                .result(true)
                                .message(messageService.getMessage("capital.allocation.approved"))
                                .code(HttpStatus.OK.value())
                                .build());
    }

    @Operation(summary = "Reject a Capital Allocation entity")
    @ApiResponse(responseCode = "200", description = "Capital Allocation Entity Rejected", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
    @PutMapping("/reject-capital-allocation/{id}")
    public ResponseEntity<ApiMessageDTO<Integer>> rejectCapitalAllocation(@PathVariable UUID id, boolean is_approved) {
        
        capitalAllocationService.reject(id);

        return ResponseEntity.ok(ApiMessageDTO.<Integer>builder()
                                .data(1)
                                .result(true)
                                .message(messageService.getMessage("capital.allocation.rejected"))
                                .code(HttpStatus.OK.value())
                                .build());
    }
}
