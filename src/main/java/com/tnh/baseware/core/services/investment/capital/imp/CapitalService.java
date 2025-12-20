package com.tnh.baseware.core.services.investment.capital.imp;

import com.tnh.baseware.core.dtos.investment.capital.CapitalDTO;
import com.tnh.baseware.core.entities.investment.capital.Capital;
import com.tnh.baseware.core.exceptions.BWCNotFoundException;
import com.tnh.baseware.core.forms.investment.capital.CapitalEditorForm;
import com.tnh.baseware.core.mappers.investment.capital.ICapitalMapper;
import com.tnh.baseware.core.repositories.investment.capital.ICapitalRepository;
import com.tnh.baseware.core.services.GenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.investment.capital.ICapitalService;
import com.tnh.baseware.core.utils.BasewareUtils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CapitalService extends
                GenericService<Capital, CapitalEditorForm, CapitalDTO, ICapitalRepository, ICapitalMapper, UUID>
                implements
                ICapitalService {

        public CapitalService(ICapitalRepository repository,
                        ICapitalMapper mapper,
                        MessageService messageService) {
                super(repository, mapper, messageService, Capital.class);
        }

        @Override
        @Transactional(isolation = Isolation.READ_COMMITTED)
        public CapitalDTO create(CapitalEditorForm form) {
                var capital = mapper.formToEntity(form);
                return mapper.entityToDTO(repository.save(capital));
        }

        @Override
        @Transactional(isolation = Isolation.READ_COMMITTED)
        public CapitalDTO update(UUID id, CapitalEditorForm form) {
                var capital = repository.findById(id).orElseThrow(
                                () -> new BWCNotFoundException(messageService.getMessage("capital.not.found", id)));
                mapper.updateCapitalFromForm(form, capital);
                return mapper.entityToDTO(repository.save(capital));
        }

        @Override
        @Transactional(readOnly = true)
        public List<CapitalDTO> findAll() {
                return mapper.mapCapitalsToTree(repository.findAll());
        }

        @Override
        @Transactional(readOnly = true)
        public Page<CapitalDTO> findAll(Pageable pageable) {
                var sortedPageable = PageRequest.of(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                Sort.by(Sort.Order.desc("createdDate")));

                var allCapitals = repository.findAllWithParent();
                var parentCapitals = repository.findAllParent("parent", sortedPageable);

                var parentMap = allCapitals.stream()
                                .filter(m -> m.getParent() != null)
                                .collect(Collectors.groupingBy(m -> m.getParent().getId()));

                var tree = parentCapitals.getContent().stream()
                                .map(m -> mapper.buildCapitalTree(m, parentMap))
                                .toList();
                return new PageImpl<>(tree, sortedPageable, parentCapitals.getTotalElements());
        }

        @Override
        @Transactional(isolation = Isolation.READ_COMMITTED)
        public void assignCapitals(UUID id, List<UUID> ids) {
                if (BasewareUtils.isBlank(ids))
                        return;

                var parent = repository.findById(id).orElseThrow(
                                () -> new BWCNotFoundException(messageService.getMessage("capital.not.found", id)));

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
        public void removeCapitals(UUID id, List<UUID> ids) {
                if (BasewareUtils.isBlank(ids))
                        return;

                var parent = repository.findById(id).orElseThrow(
                                () -> new BWCNotFoundException(messageService.getMessage("capital.not.found", id)));

                var children = repository.findAllById(ids);
                if (BasewareUtils.isBlank(children))
                        return;

                children.stream()
                                .filter(child -> parent.equals(child.getParent()))
                                .forEach(child -> child.setParent(null));

                repository.saveAll(children);
        }

}