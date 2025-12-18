package com.tnh.baseware.core.services.adu.imp;

import com.tnh.baseware.core.dtos.adu.CommuneDTO;
import com.tnh.baseware.core.entities.adu.Commune;
import com.tnh.baseware.core.forms.adu.CommuneEditorForm;
import com.tnh.baseware.core.mappers.adu.ICommuneMapper;
import com.tnh.baseware.core.repositories.adu.ICommuneRepository;
import com.tnh.baseware.core.services.GenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.adu.ICommuneService;
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
public class CommuneService extends
        GenericService<Commune, CommuneEditorForm, CommuneDTO, ICommuneRepository, ICommuneMapper, UUID> implements
        ICommuneService {

    public CommuneService(ICommuneRepository repository,
                          ICommuneMapper mapper,
                          MessageService messageService) {
        super(repository, mapper, messageService, Commune.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommuneDTO> findAllByProvinceCode(String code) {
        if (BasewareUtils.isBlank(code)) {
            return Collections.emptyList();
        }

        var communes = repository.findAllByField("provinceCode", code);
        return mapper.entitiesToDTOs(communes);
    }
}
