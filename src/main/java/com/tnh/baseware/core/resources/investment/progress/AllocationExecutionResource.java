package com.tnh.baseware.core.resources.investment.progress;

import com.tnh.baseware.core.dtos.investment.progress.AllocationExecutionDTO;
import com.tnh.baseware.core.dtos.user.ApiMessageDTO;
import com.tnh.baseware.core.entities.investment.progress.AllocationExecution;
import com.tnh.baseware.core.forms.investment.progress.AllocationExecutionEditorForm;
import com.tnh.baseware.core.properties.SystemProperties;
import com.tnh.baseware.core.resources.GenericResource;
import com.tnh.baseware.core.services.IGenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.investment.progress.IAllocationExecutionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@Tag(name = "AllocationExecutions", description = "API for managing AllocationExecution")
@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${baseware.core.system.api-prefix}/allocation-executions")
public class AllocationExecutionResource extends
        GenericResource<AllocationExecution, AllocationExecutionEditorForm, AllocationExecutionDTO, UUID> {

        IAllocationExecutionService  allocationExecutionService;

        public AllocationExecutionResource(IGenericService<AllocationExecution, AllocationExecutionEditorForm, AllocationExecutionDTO, UUID> service,
            MessageService messageService, IAllocationExecutionService  allocationExecutionService,
            SystemProperties systemProperties) {
            super(service, messageService, systemProperties.getApiPrefix() + "/allocation-executions");
            this.allocationExecutionService = allocationExecutionService;
        }


    @Operation(summary = "Get list by project")
    @ApiResponse(responseCode = "200", description = "Get list by project", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
    @GetMapping("/get-list-by-project/{id}")
    public ResponseEntity<ApiMessageDTO<List<AllocationExecutionDTO>>> getListByProjectId(@PathVariable UUID id) {
        
        List<AllocationExecutionDTO> allocationExecutions = allocationExecutionService.getListByProjectId(id);

        return ResponseEntity.ok(ApiMessageDTO.<List<AllocationExecutionDTO>>builder()
                                .data(allocationExecutions)
                                .result(true)
                                .message(messageService.getMessage("projects.retrieved"))
                                .code(HttpStatus.OK.value())
                                .build());
    }
}