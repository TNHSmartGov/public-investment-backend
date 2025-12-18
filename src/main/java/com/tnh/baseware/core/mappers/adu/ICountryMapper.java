package com.tnh.baseware.core.mappers.adu;

import com.tnh.baseware.core.dtos.adu.CountryDTO;
import com.tnh.baseware.core.entities.adu.Country;
import com.tnh.baseware.core.forms.adu.CountryEditorForm;
import com.tnh.baseware.core.mappers.IGenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICountryMapper extends IGenericMapper<Country, CountryEditorForm, CountryDTO> {
}
