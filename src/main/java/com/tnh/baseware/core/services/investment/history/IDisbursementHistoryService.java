package com.tnh.baseware.core.services.investment.history;

import java.util.UUID;

import com.tnh.baseware.core.dtos.investment.history.DisbursementHistoryDTO;
import com.tnh.baseware.core.entities.investment.history.DisbursementHistory;
import com.tnh.baseware.core.forms.investment.history.DisbursementHistoryEditorForm;
import com.tnh.baseware.core.services.IGenericService;

public interface IDisbursementHistoryService extends
        IGenericService<DisbursementHistory, DisbursementHistoryEditorForm, DisbursementHistoryDTO, UUID> {
}
