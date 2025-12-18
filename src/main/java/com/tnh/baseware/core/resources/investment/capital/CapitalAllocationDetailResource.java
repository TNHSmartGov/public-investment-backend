package com.tnh.baseware.core.resources.investment.capital;

import com.tnh.baseware.core.dtos.investment.capital.CapitalAllocationDetailDTO;
import com.tnh.baseware.core.dtos.user.ApiMessageDTO;
import com.tnh.baseware.core.entities.investment.capital.CapitalAllocationDetail;
import com.tnh.baseware.core.forms.investment.CapitalAllocationDetailEditorForm;
import com.tnh.baseware.core.properties.SystemProperties;
import com.tnh.baseware.core.resources.GenericResource;
import com.tnh.baseware.core.services.IGenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.investment.capital.ICapitalAllocationDetailService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import java.util.UUID;

@Tag(name = "capital-allocation-details", description = "API for managing CapitalAllocationDetails")
@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${baseware.core.system.api-prefix}/capital-allocation-details")
public class CapitalAllocationDetailResource extends
        GenericResource<CapitalAllocationDetail, CapitalAllocationDetailEditorForm, CapitalAllocationDetailDTO, UUID> {
            
    ICapitalAllocationDetailService capitalAllocationtDetailService;
    MessageService messageService;

    public CapitalAllocationDetailResource(IGenericService<CapitalAllocationDetail, CapitalAllocationDetailEditorForm, CapitalAllocationDetailDTO, UUID> service,
            MessageService messageService, ICapitalAllocationDetailService capitalAllocationtDetailService,
            SystemProperties systemProperties) {
        super(service, messageService, systemProperties.getApiPrefix() + "/capital-allocation-details");
        this.capitalAllocationtDetailService = capitalAllocationtDetailService;
        this.messageService = messageService;
    }

    @Operation(summary = "Delete a Capital Allocationt Detail entity")
    @ApiResponse(responseCode = "200", description = "Delete Capital Allocationt Detail Entity deleted", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
    @PostMapping("/delete-temp/{id}")
    public ResponseEntity<ApiMessageDTO<Integer>> deleteCapitalAllocationtDetail(@PathVariable UUID id) {
        
        capitalAllocationtDetailService.delete(id);

        return ResponseEntity.ok(ApiMessageDTO.<Integer>builder()
                                .data(1)
                                .result(true)
                                .message(messageService.getMessage("capital.allocation.detail.deleted"))
                                .code(HttpStatus.OK.value())
                                .build());
    }
}
