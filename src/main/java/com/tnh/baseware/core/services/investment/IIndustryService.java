package com.tnh.baseware.core.services.investment;

import java.util.UUID;

import com.tnh.baseware.core.dtos.investment.IndustryDTO;
import com.tnh.baseware.core.entities.investment.Industry;
import com.tnh.baseware.core.forms.investment.IndustryEditorForm;
import com.tnh.baseware.core.services.IGenericService;

public interface IIndustryService extends
        IGenericService<Industry, IndustryEditorForm, IndustryDTO, UUID> {
}
