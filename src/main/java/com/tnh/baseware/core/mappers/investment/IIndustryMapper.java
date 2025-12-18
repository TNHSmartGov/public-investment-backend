package com.tnh.baseware.core.mappers.investment;

import com.tnh.baseware.core.dtos.investment.IndustryDTO;
import com.tnh.baseware.core.entities.investment.Industry;
import com.tnh.baseware.core.forms.investment.IndustryEditorForm;
import com.tnh.baseware.core.mappers.IGenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IIndustryMapper  extends IGenericMapper<Industry, IndustryEditorForm, IndustryDTO> {
}

