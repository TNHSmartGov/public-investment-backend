package com.tnh.baseware.core.resources.investment;

import com.tnh.baseware.core.dtos.investment.AgencyDTO;
import com.tnh.baseware.core.dtos.user.ApiMessageDTO;
import com.tnh.baseware.core.entities.investment.Agency;
import com.tnh.baseware.core.forms.investment.AgencyEditorForm;
import com.tnh.baseware.core.properties.SystemProperties;
import com.tnh.baseware.core.resources.GenericResource;
import com.tnh.baseware.core.services.IGenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.investment.IAgencyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "Agencys", description = "API for managing Agencys")
@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${baseware.core.system.api-prefix}/agencys")
public class AgencyResource extends
        GenericResource<Agency, AgencyEditorForm, AgencyDTO, UUID> {
    
    IAgencyService agencyService;

    public AgencyResource(IGenericService<Agency, AgencyEditorForm, AgencyDTO, UUID> service,
            MessageService messageService, IAgencyService agencyService,
            SystemProperties systemProperties) {
        super(service, messageService, systemProperties.getApiPrefix() + "/capitals");
        this.agencyService = agencyService;
    }

    @Operation(summary = "Assign agencys to a parent unit")
    @ApiResponse(responseCode = "200", description = "Agencys assigned", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
    @PostMapping("/{id}/assign-agencys")
    public ResponseEntity<ApiMessageDTO<Integer>> assignAgencys(@PathVariable UUID id,
                    @RequestBody List<UUID> ids) {
            agencyService.assignAgencys(id, ids);
            return ResponseEntity.ok(ApiMessageDTO.<Integer>builder()
                            .data(1)
                            .result(true)
                            .message(messageService.getMessage("agencys.assigned"))
                            .code(HttpStatus.OK.value())
                            .build());
    }

    @Operation(summary = "Remove agencys from a parent unit")
    @ApiResponse(responseCode = "200", description = "Agencys removed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
    @DeleteMapping("/{id}/remove-agencys")
    public ResponseEntity<ApiMessageDTO<Integer>> removeAgencys(@PathVariable UUID id,
                    @RequestBody List<UUID> ids) {
            agencyService.removeAgencys(id, ids);
            return ResponseEntity.ok(ApiMessageDTO.<Integer>builder()
                            .data(1)
                            .result(true)
                            .message(messageService.getMessage("agencys.removed"))
                            .code(HttpStatus.OK.value())
                            .build());
    }
}
