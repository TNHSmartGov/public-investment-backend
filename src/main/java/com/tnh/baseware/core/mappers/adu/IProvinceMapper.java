package com.tnh.baseware.core.mappers.adu;

import com.tnh.baseware.core.dtos.adu.ProvinceDTO;
import com.tnh.baseware.core.entities.adu.Province;
import com.tnh.baseware.core.forms.adu.ProvinceEditorForm;
import com.tnh.baseware.core.mappers.IGenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IProvinceMapper extends IGenericMapper<Province, ProvinceEditorForm, ProvinceDTO> {
}
