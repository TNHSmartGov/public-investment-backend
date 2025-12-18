package com.tnh.baseware.core.resources.audit;

import com.tnh.baseware.core.dtos.audit.TrackActivityDTO;
import com.tnh.baseware.core.dtos.audit.TrackActivityDetailDTO;
import com.tnh.baseware.core.dtos.user.ApiMessageDTO;
import com.tnh.baseware.core.entities.audit.TrackActivity;
import com.tnh.baseware.core.forms.audit.TrackActivityEditorForm;
import com.tnh.baseware.core.properties.SystemProperties;
import com.tnh.baseware.core.resources.GenericResource;
import com.tnh.baseware.core.services.IGenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.audit.ITrackActivityService;

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

import java.util.UUID;

@Tag(name = "Track Activities", description = "API for managing track activities")
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("${baseware.core.system.api-prefix}/track-activities")
public class TrackActivityResource extends
        GenericResource<TrackActivity, TrackActivityEditorForm, TrackActivityDTO, UUID> {

    ITrackActivityService trackActivityService;

    public TrackActivityResource(
            IGenericService<TrackActivity, TrackActivityEditorForm, TrackActivityDTO, UUID> service,
            MessageService messageService,
            ITrackActivityService trackActivityService,
            SystemProperties systemProperties) {
        super(service, messageService, systemProperties.getApiPrefix() + "/track-activities");
        this.trackActivityService = trackActivityService;
    }

    @Operation(summary = "Get track activity detail")
    @ApiResponse(responseCode = "200", description = "Track activity detail retrieved", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
    @GetMapping("/{id}/detail")
    public ResponseEntity<ApiMessageDTO<TrackActivityDetailDTO>> getTrackActivityDetail(@PathVariable UUID id) {
        var trackActivityDetailDTO = trackActivityService.getTrackActivityDetail(id);
        return ResponseEntity.ok(ApiMessageDTO.<TrackActivityDetailDTO>builder()
                .data(trackActivityDetailDTO)
                .result(true)
                .message(messageService.getMessage("track.activity.detail.found"))
                .code(HttpStatus.OK.value())
                .build());
    }
}
