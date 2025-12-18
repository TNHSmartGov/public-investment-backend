package com.tnh.baseware.core.services.investment.construction;

import java.util.UUID;

import com.tnh.baseware.core.dtos.investment.construction.LandClearanceDTO;
import com.tnh.baseware.core.entities.investment.construction.LandClearance;
import com.tnh.baseware.core.forms.investment.construction.LandClearanceEditorForm;
import com.tnh.baseware.core.services.IGenericService;

public interface ILandClearanceService extends
        IGenericService<LandClearance, LandClearanceEditorForm, LandClearanceDTO, UUID> {
}
