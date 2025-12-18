package com.tnh.baseware.core.services.investment;

import java.util.UUID;

import com.tnh.baseware.core.dtos.investment.ApprovalDecisionDTO;
import com.tnh.baseware.core.entities.investment.ApprovalDecision;
import com.tnh.baseware.core.forms.investment.ApprovalDecisionEditorForm;
import com.tnh.baseware.core.services.IGenericService;

public interface IApprovalDecisionService extends
        IGenericService<ApprovalDecision, ApprovalDecisionEditorForm, ApprovalDecisionDTO, UUID> {

        @Override
        ApprovalDecisionDTO create(ApprovalDecisionEditorForm form);

        @Override
        ApprovalDecisionDTO update(UUID id, ApprovalDecisionEditorForm form);

}
