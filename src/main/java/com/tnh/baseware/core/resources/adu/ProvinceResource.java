package com.tnh.baseware.core.resources.adu;

import com.tnh.baseware.core.dtos.adu.ProvinceDTO;
import com.tnh.baseware.core.dtos.user.ApiMessageDTO;
import com.tnh.baseware.core.entities.adu.Province;
import com.tnh.baseware.core.forms.adu.ProvinceEditorForm;
import com.tnh.baseware.core.properties.SystemProperties;
import com.tnh.baseware.core.resources.GenericResource;
import com.tnh.baseware.core.services.IGenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.adu.IProvinceService;

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

@Tag(name = "Provinces", description = "API for managing provinces")
@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${baseware.core.system.api-prefix}/provinces")
public class ProvinceResource extends
        GenericResource<Province, ProvinceEditorForm, ProvinceDTO, UUID> {

    IProvinceService provinceService;

    public ProvinceResource(IGenericService<Province, ProvinceEditorForm, ProvinceDTO, UUID> service,
            MessageService messageService,
            IProvinceService provinceService,
            SystemProperties systemProperties) {
        super(service, messageService, systemProperties.getApiPrefix() + "/provinces");
        this.provinceService = provinceService;
    }

    @Operation(summary = "Find all provinces by country code")
    @ApiResponse(responseCode = "200", description = "Provinces found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
    @GetMapping("/by-country/{code}")
    public ResponseEntity<ApiMessageDTO<List<ProvinceDTO>>> findAllByCountryCode(@PathVariable String code) {
        var provinces = provinceService.findAllByCountryCode(code);
        return ResponseEntity.ok(ApiMessageDTO.<List<ProvinceDTO>>builder()
                .data(provinces)
                .result(true)
                .message(messageService.getMessage("provinces.retrieved"))
                .code(HttpStatus.OK.value())
                .build());
    }
}
