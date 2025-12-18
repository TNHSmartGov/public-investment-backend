package com.tnh.baseware.core.resources.adu;

import com.tnh.baseware.core.dtos.adu.CommuneDTO;
import com.tnh.baseware.core.dtos.user.ApiMessageDTO;
import com.tnh.baseware.core.entities.adu.Commune;
import com.tnh.baseware.core.forms.adu.CommuneEditorForm;
import com.tnh.baseware.core.properties.SystemProperties;
import com.tnh.baseware.core.resources.GenericResource;
import com.tnh.baseware.core.services.IGenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.adu.ICommuneService;

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

@Tag(name = "Communes", description = "API for managing communes")
@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${baseware.core.system.api-prefix}/communes")
public class CommuneResource extends
        GenericResource<Commune, CommuneEditorForm, CommuneDTO, UUID> {

    ICommuneService communeService;

    public CommuneResource(IGenericService<Commune, CommuneEditorForm, CommuneDTO, UUID> service,
            MessageService messageService,
            ICommuneService communeService,
            SystemProperties systemProperties) {
        super(service, messageService, systemProperties.getApiPrefix() + "/communes");
        this.communeService = communeService;
    }

    @Operation(summary = "Find all communes by province code")
    @ApiResponse(responseCode = "200", description = "Communes found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
    @GetMapping("/by-province/{code}")
    public ResponseEntity<ApiMessageDTO<List<CommuneDTO>>> findAllByProvinceCode(@PathVariable String code) {
        var communes = communeService.findAllByProvinceCode(code);
        return ResponseEntity.ok(ApiMessageDTO.<List<CommuneDTO>>builder()
                .data(communes)
                .result(true)
                .message(messageService.getMessage("communes.retrieved"))
                .code(HttpStatus.OK.value())
                .build());
    }
}
