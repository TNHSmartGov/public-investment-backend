package com.tnh.baseware.core.services.adu.imp;

import com.tnh.baseware.core.dtos.adu.ProvinceDTO;
import com.tnh.baseware.core.entities.adu.Province;
import com.tnh.baseware.core.forms.adu.ProvinceEditorForm;
import com.tnh.baseware.core.mappers.adu.IProvinceMapper;
import com.tnh.baseware.core.repositories.adu.IProvinceRepository;
import com.tnh.baseware.core.services.GenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.adu.IProvinceService;
import com.tnh.baseware.core.utils.BasewareUtils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProvinceService extends
        GenericService<Province, ProvinceEditorForm, ProvinceDTO, IProvinceRepository, IProvinceMapper, UUID>
        implements
        IProvinceService {

    public ProvinceService(IProvinceRepository repository,
                           IProvinceMapper mapper,
                           MessageService messageService) {
        super(repository, mapper, messageService, Province.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProvinceDTO> findAllByCountryCode(String code) {
        if (BasewareUtils.isBlank(code)) {
            return Collections.emptyList();
        }

        var provinces = repository.findAllByField("countryCode", code);
        return mapper.entitiesToDTOs(provinces);
    }
}
