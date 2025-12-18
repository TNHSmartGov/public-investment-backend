package com.tnh.baseware.core.mappers.adu;

import com.tnh.baseware.core.dtos.adu.CommuneDTO;
import com.tnh.baseware.core.entities.adu.Commune;
import com.tnh.baseware.core.forms.adu.CommuneEditorForm;
import com.tnh.baseware.core.mappers.IGenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICommuneMapper extends IGenericMapper<Commune, CommuneEditorForm, CommuneDTO> {
}
