package com.tnh.baseware.core.services.user.imp;

import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.dtos.user.MenuDTO;
import com.tnh.baseware.core.entities.user.Menu;
import com.tnh.baseware.core.exceptions.BWCNotFoundException;
import com.tnh.baseware.core.forms.user.MenuEditorForm;
import com.tnh.baseware.core.mappers.user.IMenuMapper;
import com.tnh.baseware.core.repositories.audit.ICategoryRepository;
import com.tnh.baseware.core.repositories.user.IMenuRepository;
import com.tnh.baseware.core.repositories.user.IRoleRepository;
import com.tnh.baseware.core.services.GenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.user.IMenuService;
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
public class MenuService extends
        GenericService<Menu, MenuEditorForm, MenuDTO, IMenuRepository, IMenuMapper, UUID> implements
        IMenuService {

    IRoleRepository roleRepository;
    ICategoryRepository categoryRepository;
    GenericEntityFetcher fetcher;

    public MenuService(IMenuRepository repository,
                       IMenuMapper mapper,
                       MessageService messageService,
                       IRoleRepository roleRepository,
                       ICategoryRepository categoryRepository,
                       GenericEntityFetcher fetcher) {
        super(repository, mapper, messageService, Menu.class);
        this.roleRepository = roleRepository;
        this.categoryRepository = categoryRepository;
        this.fetcher = fetcher;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public MenuDTO create(MenuEditorForm form) {
        var menu = mapper.formToEntity(form, fetcher, categoryRepository);
        return mapper.entityToDTO(repository.save(menu));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public MenuDTO update(UUID id, MenuEditorForm form) {
        var menu = repository.findById(id).orElseThrow(() ->
                new BWCNotFoundException(messageService.getMessage("menu.not.found", id)));
        mapper.updateMenuFromForm(form, menu, fetcher, categoryRepository);
        return mapper.entityToDTO(repository.save(menu));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuDTO> findAll() {
        return mapper.mapMenusToTree(repository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MenuDTO> findAll(Pageable pageable) {
        var sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Order.desc("createdDate"))
        );

        var allMenus = repository.findAllWithParent();
        var parentMenus = repository.findAllParent("parent", sortedPageable);

        var parentMap = allMenus.stream()
                .filter(m -> m.getParent() != null)
                .collect(Collectors.groupingBy(m -> m.getParent().getId()));

        var tree = parentMenus.getContent().stream()
                .map(m -> mapper.buildMenuTree(m, parentMap))
                .toList();
        return new PageImpl<>(tree, sortedPageable, parentMenus.getTotalElements());
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void assignMenus(UUID id, List<UUID> ids) {
        if (BasewareUtils.isBlank(ids)) return;

        var parent = repository.findById(id).orElseThrow(() ->
                new BWCNotFoundException(messageService.getMessage("menu.not.found", id)));

        var children = repository.findAllById(ids);
        if (BasewareUtils.isBlank(children)) return;

        children.stream()
                .filter(child -> !parent.equals(child.getParent()))
                .forEach(child -> child.setParent(parent));

        repository.saveAll(children);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void removeMenus(UUID id, List<UUID> ids) {
        if (BasewareUtils.isBlank(ids)) return;

        var parent = repository.findById(id).orElseThrow(() ->
                new BWCNotFoundException(messageService.getMessage("menu.not.found", id)));

        var children = repository.findAllById(ids);
        if (BasewareUtils.isBlank(children)) return;

        children.stream()
                .filter(child -> parent.equals(child.getParent()))
                .forEach(child -> child.setParent(null));

        repository.saveAll(children);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void assignRoles(UUID id, List<UUID> ids) {
        if (BasewareUtils.isBlank(ids)) return;

        var menu = repository.findById(id).orElseThrow(() ->
                new BWCNotFoundException(messageService.getMessage("menu.not.found", id)));

        var roles = roleRepository.findAllById(ids);
        if (BasewareUtils.isBlank(roles)) return;

        menu.getRoles().addAll(roles);
        repository.save(menu);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void removeRoles(UUID id, List<UUID> ids) {
        if (BasewareUtils.isBlank(ids)) return;

        var menu = repository.findById(id).orElseThrow(() ->
                new BWCNotFoundException(messageService.getMessage("menu.not.found", id)));

        var roles = roleRepository.findAllById(ids);
        if (BasewareUtils.isBlank(roles)) return;

        roles.forEach(menu.getRoles()::remove);
        repository.save(menu);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean hasAccess(UUID menuId, UUID roleId) {
        var menu = repository.findById(menuId).orElseThrow(() ->
                new BWCNotFoundException(messageService.getMessage("menu.not.found", menuId)));
        var role = roleRepository.findById(roleId).orElseThrow(() ->
                new BWCNotFoundException(messageService.getMessage("role.not.found", roleId)));
        return menu.getRoles().contains(role);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuDTO> findAllWithoutRoles() {
        return repository.findAllByEntitiesIsEmpty("roles")
                .stream()
                .map(mapper::entityToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuDTO> findAllWithoutRole(UUID id) {
        var role = roleRepository.findById(id).orElseThrow(() ->
                new BWCNotFoundException(messageService.getMessage("role.not.found", id)));

        return repository.findAllByEntitiesNotContaining("roles", role)
                .stream()
                .map(mapper::entityToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuDTO> findAllByRole(UUID id) {
        var role = roleRepository.findById(id).orElseThrow(() ->
                new BWCNotFoundException(messageService.getMessage("role.not.found", id)));
        return repository.findAllByEntitiesContaining("roles", role).stream()
                .map(mapper::entityToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MenuDTO> findAllByRole(UUID id, Pageable pageable) {
        var role = roleRepository.findById(id).orElseThrow(() ->
                new BWCNotFoundException(messageService.getMessage("role.not.found", id)));
        return repository.findAllByEntitiesContaining("roles", role, pageable).map(mapper::entityToDTO);
    }
}
