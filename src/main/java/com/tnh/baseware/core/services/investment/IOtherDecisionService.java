package com.tnh.baseware.core.services.investment;

import java.util.UUID;

import com.tnh.baseware.core.dtos.investment.OtherDecisionDTO;
import com.tnh.baseware.core.entities.investment.OtherDecision;
import com.tnh.baseware.core.forms.investment.OtherDecisionEditorForm;
import com.tnh.baseware.core.services.IGenericService;

public interface IOtherDecisionService extends
        IGenericService<OtherDecision, OtherDecisionEditorForm, OtherDecisionDTO, UUID> {

        @Override
        OtherDecisionDTO create(OtherDecisionEditorForm form);

        @Override
        OtherDecisionDTO update(UUID id, OtherDecisionEditorForm form);

}
