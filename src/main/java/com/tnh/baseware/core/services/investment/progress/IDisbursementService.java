package com.tnh.baseware.core.services.investment.progress;

import java.util.List;
import java.util.UUID;

import com.tnh.baseware.core.dtos.investment.progress.DisbursementDTO;
import com.tnh.baseware.core.entities.investment.progress.Disbursement;
import com.tnh.baseware.core.forms.investment.progress.DisbursementEditorForm;
import com.tnh.baseware.core.services.IGenericService;

public interface IDisbursementService extends
        IGenericService<Disbursement, DisbursementEditorForm, DisbursementDTO, UUID> {

        List<DisbursementDTO> getListByProjectId(UUID id);
}
