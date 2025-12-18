package com.tnh.baseware.core.services.investment.capital;

import java.util.List;
import java.util.UUID;

import com.tnh.baseware.core.dtos.investment.capital.CapitalAllocationDetailDTO;
import com.tnh.baseware.core.entities.investment.capital.CapitalAllocationDetail;
import com.tnh.baseware.core.forms.investment.CapitalAllocationDetailEditorForm;
import com.tnh.baseware.core.services.IGenericService;

public interface ICapitalAllocationDetailService extends
        IGenericService<CapitalAllocationDetail, CapitalAllocationDetailEditorForm, CapitalAllocationDetailDTO, UUID> {

        @Override
        CapitalAllocationDetailDTO create(CapitalAllocationDetailEditorForm form);

        @Override
        CapitalAllocationDetailDTO update(UUID id, CapitalAllocationDetailEditorForm form);
        
}
