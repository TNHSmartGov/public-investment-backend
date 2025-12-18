package com.tnh.baseware.core.services.investment;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tnh.baseware.core.dtos.investment.AgencyDTO;
import com.tnh.baseware.core.entities.investment.Agency;
import com.tnh.baseware.core.forms.investment.AgencyEditorForm;
import com.tnh.baseware.core.services.IGenericService;

public interface IAgencyService extends
        IGenericService<Agency, AgencyEditorForm, AgencyDTO, UUID> {

    @Override
    AgencyDTO create(AgencyEditorForm form);

    @Override
    AgencyDTO update(UUID id, AgencyEditorForm form);

    @Override
    List<AgencyDTO> findAll();

    @Override
    Page<AgencyDTO> findAll(Pageable pageable);

    void assignAgencys(UUID id, List<UUID> ids);

    void removeAgencys(UUID id, List<UUID> ids);

}
