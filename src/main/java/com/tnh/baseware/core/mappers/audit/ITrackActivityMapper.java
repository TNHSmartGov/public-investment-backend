package com.tnh.baseware.core.mappers.audit;

import com.tnh.baseware.core.dtos.audit.TrackActivityDTO;
import com.tnh.baseware.core.entities.audit.TrackActivity;
import com.tnh.baseware.core.forms.audit.TrackActivityEditorForm;
import com.tnh.baseware.core.mappers.IGenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ITrackActivityMapper extends IGenericMapper<TrackActivity, TrackActivityEditorForm, TrackActivityDTO> {
}
