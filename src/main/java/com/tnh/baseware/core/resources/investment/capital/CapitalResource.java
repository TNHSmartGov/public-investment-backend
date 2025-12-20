package com.tnh.baseware.core.resources.investment.capital;

import com.tnh.baseware.core.dtos.investment.capital.CapitalDTO;
import com.tnh.baseware.core.dtos.user.ApiMessageDTO;
import com.tnh.baseware.core.entities.investment.capital.Capital;
import com.tnh.baseware.core.forms.investment.capital.CapitalEditorForm;
import com.tnh.baseware.core.properties.SystemProperties;
import com.tnh.baseware.core.resources.GenericResource;
import com.tnh.baseware.core.services.IGenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.investment.capital.ICapitalService;

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

@Tag(name = "Capitals", description = "API for managing capitals")
@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${baseware.core.system.api-prefix}/capitals")

public class CapitalResource extends
        GenericResource<Capital, CapitalEditorForm, CapitalDTO, UUID> {
    
    ICapitalService capitalService;

    public CapitalResource(IGenericService<Capital, CapitalEditorForm, CapitalDTO, UUID> service,
            MessageService messageService, ICapitalService capitalService,
            SystemProperties systemProperties) {
        super(service, messageService, systemProperties.getApiPrefix() + "/capitals");
        this.capitalService = capitalService;
    }

    @Operation(summary = "Assign capitals to a parent unit")
    @ApiResponse(responseCode = "200", description = "Capitals assigned", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
    @PostMapping("/{id}/assign-capitals")
    public ResponseEntity<ApiMessageDTO<Integer>> assignCapitals(@PathVariable UUID id,
                    @RequestBody List<UUID> ids) {
            capitalService.assignCapitals(id, ids);
            return ResponseEntity.ok(ApiMessageDTO.<Integer>builder()
                            .data(1)
                            .result(true)
                            .message(messageService.getMessage("capitals.assigned"))
                            .code(HttpStatus.OK.value())
                            .build());
    }

    @Operation(summary = "Remove capitals from a parent unit")
    @ApiResponse(responseCode = "200", description = "Capitals removed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
    @DeleteMapping("/{id}/remove-capitals")
    public ResponseEntity<ApiMessageDTO<Integer>> removeCapitals(@PathVariable UUID id,
                    @RequestBody List<UUID> ids) {
            capitalService.removeCapitals(id, ids);
            return ResponseEntity.ok(ApiMessageDTO.<Integer>builder()
                            .data(1)
                            .result(true)
                            .message(messageService.getMessage("capitals.removed"))
                            .code(HttpStatus.OK.value())
                            .build());
    }
}
