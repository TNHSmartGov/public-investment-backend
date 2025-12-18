package com.tnh.baseware.core.services.adu;

import com.tnh.baseware.core.dtos.adu.CommuneDTO;
import com.tnh.baseware.core.entities.adu.Commune;
import com.tnh.baseware.core.forms.adu.CommuneEditorForm;
import com.tnh.baseware.core.services.IGenericService;

import java.util.List;
import java.util.UUID;

public interface ICommuneService extends
        IGenericService<Commune, CommuneEditorForm, CommuneDTO, UUID> {

    List<CommuneDTO> findAllByProvinceCode(String code);
}
