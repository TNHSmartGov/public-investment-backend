package com.tnh.baseware.core.services.user;

import com.tnh.baseware.core.dtos.user.MenuDTO;
import com.tnh.baseware.core.entities.user.Menu;
import com.tnh.baseware.core.forms.user.MenuEditorForm;
import com.tnh.baseware.core.services.IGenericService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IMenuService extends IGenericService<Menu, MenuEditorForm, MenuDTO, UUID> {

    @Override
    MenuDTO create(MenuEditorForm form);

    @Override
    MenuDTO update(UUID id, MenuEditorForm form);

    @Override
    List<MenuDTO> findAll();

    @Override
    Page<MenuDTO> findAll(Pageable pageable);

    void assignMenus(UUID id, List<UUID> ids);

    void removeMenus(UUID id, List<UUID> ids);

    void assignRoles(UUID id, List<UUID> ids);

    void removeRoles(UUID id, List<UUID> ids);

    boolean hasAccess(UUID menuId, UUID roleId);

    List<MenuDTO> findAllWithoutRoles();

    List<MenuDTO> findAllWithoutRole(UUID id);

    List<MenuDTO> findAllByRole(UUID id);

    Page<MenuDTO> findAllByRole(UUID id, Pageable pageable);
}