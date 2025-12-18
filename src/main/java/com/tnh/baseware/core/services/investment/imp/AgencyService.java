package com.tnh.baseware.core.services.investment.imp;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import com.tnh.baseware.core.components.EnumRegistry;
import com.tnh.baseware.core.dtos.investment.AgencyDTO;
import com.tnh.baseware.core.entities.investment.Agency;
import com.tnh.baseware.core.exceptions.BWCNotFoundException;
import com.tnh.baseware.core.forms.investment.AgencyEditorForm;
import com.tnh.baseware.core.mappers.investment.IAgencyMapper;
import com.tnh.baseware.core.repositories.investment.IAgencyRepository;
import com.tnh.baseware.core.services.GenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.investment.IAgencyService;
import com.tnh.baseware.core.utils.BasewareUtils;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AgencyService extends
                GenericService<Agency, AgencyEditorForm, AgencyDTO, IAgencyRepository, IAgencyMapper, UUID>
                implements
                IAgencyService {

        public AgencyService(IAgencyRepository repository,
                        IAgencyMapper mapper,
                        MessageService messageService,
                        EnumRegistry enumRegistry) {
                super(repository, mapper, messageService, Agency.class, enumRegistry);
        }

        @Override
        @Transactional(isolation = Isolation.READ_COMMITTED)
        public AgencyDTO create(AgencyEditorForm form) {
                var agency = mapper.formToEntity(form);
                return mapper.entityToDTO(repository.save(agency));
        }

        @Override
        @Transactional(isolation = Isolation.READ_COMMITTED)
        public AgencyDTO update(UUID id, AgencyEditorForm form) {
                var agency = repository.findById(id).orElseThrow(
                                () -> new BWCNotFoundException(messageService.getMessage("agency.not.found", id)));
                mapper.updateAgencyFromForm(form, agency);
                return mapper.entityToDTO(repository.save(agency));
        }

        @Override
        @Transactional(readOnly = true)
        public List<AgencyDTO> findAll() {
                return mapper.mapAgencysToTree(repository.findAll());
        }

        @Override
        @Transactional(readOnly = true)
        public Page<AgencyDTO> findAll(Pageable pageable) {
                var sortedPageable = PageRequest.of(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                Sort.by(Sort.Order.desc("createdDate")));

                var allAgencys = repository.findAllWithParent();
                var parentAgencys = repository.findAllParent("parent", sortedPageable);

                var parentMap = allAgencys.stream()
                                .filter(m -> m.getParent() != null)
                                .collect(Collectors.groupingBy(m -> m.getParent().getId()));

                var tree = parentAgencys.getContent().stream()
                                .map(m -> mapper.buildAgencyTree(m, parentMap))
                                .toList();
                return new PageImpl<>(tree, sortedPageable, parentAgencys.getTotalElements());
        }

        @Override
        @Transactional(isolation = Isolation.READ_COMMITTED)
        public void assignAgencys(UUID id, List<UUID> ids) {
                if (BasewareUtils.isBlank(ids))
                        return;

                var parent = repository.findById(id).orElseThrow(
                                () -> new BWCNotFoundException(messageService.getMessage("agency.not.found", id)));

                var children = repository.findAllById(ids);
                if (BasewareUtils.isBlank(children))
                        return;

                children.stream()
                                .filter(child -> !parent.equals(child.getParent()))
                                .forEach(child -> child.setParent(parent));

                repository.saveAll(children);
        }

        @Override
        @Transactional(isolation = Isolation.READ_COMMITTED)
        public void removeAgencys(UUID id, List<UUID> ids) {
                if (BasewareUtils.isBlank(ids))
                        return;

                var parent = repository.findById(id).orElseThrow(
                                () -> new BWCNotFoundException(messageService.getMessage("agency.not.found", id)));

                var children = repository.findAllById(ids);
                if (BasewareUtils.isBlank(children))
                        return;

                children.stream()
                                .filter(child -> parent.equals(child.getParent()))
                                .forEach(child -> child.setParent(null));

                repository.saveAll(children);
        }

}
