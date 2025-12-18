package com.tnh.baseware.core.services.user;

import com.tnh.baseware.core.dtos.user.RoleDTO;
import com.tnh.baseware.core.entities.user.Role;
import com.tnh.baseware.core.forms.user.RoleEditorForm;
import com.tnh.baseware.core.services.IGenericService;

import java.util.List;
import java.util.UUID;

public interface IRoleService extends IGenericService<Role, RoleEditorForm, RoleDTO, UUID> {

    @Override
    void delete(UUID id);

    void assignUsers(UUID id, List<UUID> ids);

    void removeUsers(UUID id, List<UUID> ids);

    void assignPrivileges(UUID id, List<UUID> ids);

    void removePrivileges(UUID id, List<UUID> ids);

    void assignMenus(UUID id, List<UUID> ids);

    void removeMenus(UUID id, List<UUID> ids);

    List<RoleDTO> findAllByMenu(UUID id);

    List<RoleDTO> findAllByPrivilege(UUID id);
}
