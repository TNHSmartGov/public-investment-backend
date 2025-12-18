package com.tnh.baseware.core.services.adu;

import com.tnh.baseware.core.dtos.adu.ProvinceDTO;
import com.tnh.baseware.core.entities.adu.Province;
import com.tnh.baseware.core.forms.adu.ProvinceEditorForm;
import com.tnh.baseware.core.services.IGenericService;

import java.util.List;
import java.util.UUID;

public interface IProvinceService extends
        IGenericService<Province, ProvinceEditorForm, ProvinceDTO, UUID> {

    List<ProvinceDTO> findAllByCountryCode(String code);
}
