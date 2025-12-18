package com.tnh.baseware.core.services.audit;

import com.tnh.baseware.core.dtos.audit.TrackActivityDTO;
import com.tnh.baseware.core.dtos.audit.TrackActivityDetailDTO;
import com.tnh.baseware.core.entities.audit.TrackActivity;
import com.tnh.baseware.core.forms.audit.TrackActivityEditorForm;
import com.tnh.baseware.core.services.IGenericService;

import java.util.UUID;

public interface ITrackActivityService
        extends IGenericService<TrackActivity, TrackActivityEditorForm, TrackActivityDTO, UUID> {

    TrackActivityDetailDTO getTrackActivityDetail(UUID id);

    void trackLogin(String ip, String device, String username);

    void trackLogout(String ip, String device, String username);

    void trackRefreshToken(String ip, String device, String username);
}
