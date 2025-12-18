package com.tnh.baseware.core.services.adu;

import com.tnh.baseware.core.dtos.adu.CountryDTO;
import com.tnh.baseware.core.entities.adu.Country;
import com.tnh.baseware.core.forms.adu.CountryEditorForm;
import com.tnh.baseware.core.services.IGenericService;

import java.util.UUID;

public interface ICountryService extends IGenericService<Country, CountryEditorForm, CountryDTO, UUID> {
}
