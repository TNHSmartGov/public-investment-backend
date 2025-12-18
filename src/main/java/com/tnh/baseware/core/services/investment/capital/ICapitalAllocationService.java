package com.tnh.baseware.core.services.investment.capital;
import java.util.UUID;
import java.util.List;
import com.tnh.baseware.core.dtos.investment.capital.CapitalAllocationDTO;
import com.tnh.baseware.core.entities.investment.capital.CapitalAllocation;
import com.tnh.baseware.core.forms.investment.CapitalAllocationEditorForm;
import com.tnh.baseware.core.services.IGenericService;

public interface ICapitalAllocationService extends
        IGenericService<CapitalAllocation, CapitalAllocationEditorForm, CapitalAllocationDTO, UUID> {

        @Override
        CapitalAllocationDTO create(CapitalAllocationEditorForm form);

        @Override
        CapitalAllocationDTO update(UUID id, CapitalAllocationEditorForm form);

        List<CapitalAllocationDTO> createMany(List<CapitalAllocationEditorForm> forms);

        boolean approve(UUID id);

        boolean reject(UUID id);
}
