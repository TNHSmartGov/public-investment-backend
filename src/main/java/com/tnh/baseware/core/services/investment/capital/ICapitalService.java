package com.tnh.baseware.core.services.investment.capital;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tnh.baseware.core.dtos.investment.capital.CapitalDTO;
import com.tnh.baseware.core.entities.investment.capital.Capital;
import com.tnh.baseware.core.forms.investment.CapitalEditorForm;
import com.tnh.baseware.core.services.IGenericService;

public interface ICapitalService extends
        IGenericService<Capital, CapitalEditorForm, CapitalDTO, UUID> {

    @Override
    CapitalDTO create(CapitalEditorForm form);

    @Override
    CapitalDTO update(UUID id, CapitalEditorForm form);

    @Override
    List<CapitalDTO> findAll();

    @Override
    Page<CapitalDTO> findAll(Pageable pageable);

    void assignCapitals(UUID id, List<UUID> ids);

    void removeCapitals(UUID id, List<UUID> ids);

}
