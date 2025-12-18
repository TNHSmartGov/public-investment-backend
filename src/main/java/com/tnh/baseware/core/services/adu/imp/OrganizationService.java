package com.tnh.baseware.core.services.adu.imp;

import com.tnh.baseware.core.dtos.adu.OrganizationDTO;
import com.tnh.baseware.core.entities.adu.Organization;
import com.tnh.baseware.core.exceptions.BWCNotFoundException;
import com.tnh.baseware.core.forms.adu.OrganizationEditorForm;
import com.tnh.baseware.core.mappers.adu.IOrganizationMapper;
import com.tnh.baseware.core.repositories.adu.IOrganizationRepository;
import com.tnh.baseware.core.repositories.user.IUserRepository;
import com.tnh.baseware.core.services.GenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.adu.IOrganizationService;
import com.tnh.baseware.core.utils.BasewareUtils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OrganizationService extends
        GenericService<Organization, OrganizationEditorForm, OrganizationDTO, IOrganizationRepository, IOrganizationMapper, UUID>
        implements
        IOrganizationService {

    IUserRepository userRepository;

    public OrganizationService(IOrganizationRepository repository,
                               IOrganizationMapper mapper,
                               MessageService messageService,
                               IUserRepository userRepository) {
        super(repository, mapper, messageService, Organization.class);
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationDTO> findAll() {
        var organizations = repository.findAll();
        return mapper.mapOrganizationsToTree(organizations);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrganizationDTO> findAll(Pageable pageable) {
        var sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Order.desc("createdDate")));

        var allOrganizations = repository.findAllWithParent();
        var parentOrganizations = repository.findAllParent("parent", sortedPageable);

        var parentMap = allOrganizations.stream()
                .filter(o -> o.getParent() != null)
                .collect(Collectors.groupingBy(o -> o.getParent().getId()));

        var tree = parentOrganizations.getContent().stream()
                .map(o -> mapper.buildOrganizationTree(o, parentMap))
                .toList();

        return new PageImpl<>(tree, sortedPageable, parentOrganizations.getTotalElements());
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void assignOrganizations(UUID id, List<UUID> ids) {
        if (BasewareUtils.isBlank(ids)) {
            return;
        }

        var parent = repository.findById(id).orElseThrow(() ->
                new BWCNotFoundException(messageService.getMessage("organization.not.found", id)));

        var children = repository.findAllById(ids);
        if (BasewareUtils.isBlank(children)) {
            return;
        }

        children.stream()
                .filter(child -> !parent.equals(child.getParent()))
                .forEach(child -> child.setParent(parent));

        repository.saveAll(children);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void removeOrganizations(UUID id, List<UUID> ids) {
        if (BasewareUtils.isBlank(ids)) {
            return;
        }

        var parent = repository.findById(id).orElseThrow(() ->
                new BWCNotFoundException(messageService.getMessage("organization.not.found", id)));

        var children = repository.findAllById(ids);
        if (BasewareUtils.isBlank(children)) {
            return;
        }

        children.stream()
                .filter(child -> parent.equals(child.getParent()))
                .forEach(child -> child.setParent(null));

        repository.saveAll(children);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void assignUsers(UUID id, List<UUID> ids) {
        if (BasewareUtils.isBlank(ids)) {
            return;
        }

        var organization = repository.findById(id).orElseThrow(() ->
                new BWCNotFoundException(messageService.getMessage("organization.not.found", id)));

        var users = userRepository.findAllById(ids);
        if (BasewareUtils.isBlank(users)) {
            return;
        }

        users.forEach(user -> user.getOrganizations().add(organization));
        userRepository.saveAll(users);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void removeUsers(UUID id, List<UUID> ids) {
        if (BasewareUtils.isBlank(ids)) {
            return;
        }

        var organization = repository.findById(id).orElseThrow(() ->
                new BWCNotFoundException(messageService.getMessage("organization.not.found", id)));

        var users = userRepository.findAllById(ids);
        if (BasewareUtils.isBlank(users)) {
            return;
        }

        users.forEach(user -> user.getOrganizations().remove(organization));
        userRepository.saveAll(users);
    }
}
