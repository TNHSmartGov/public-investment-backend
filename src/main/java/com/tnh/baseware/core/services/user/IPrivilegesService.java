package com.tnh.baseware.core.services.user;

import com.tnh.baseware.core.dtos.user.PrivilegeDTO;
import com.tnh.baseware.core.entities.user.Privilege;
import com.tnh.baseware.core.forms.user.PrivilegeEditorForm;
import com.tnh.baseware.core.services.IGenericService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IPrivilegesService extends IGenericService<Privilege, PrivilegeEditorForm, PrivilegeDTO, UUID> {

    @Override
    PrivilegeDTO update(UUID id, PrivilegeEditorForm form);

    @Override
    void delete(UUID id);

    void syncPrivileges();

    List<String> findAllResourceNames();

    List<PrivilegeDTO> findAllByResourceName(String resourceName);

    Page<PrivilegeDTO> findAllByResourceName(String resourceName, Pageable pageable);

    List<PrivilegeDTO> findAllByRole(UUID id);

    Page<PrivilegeDTO> findAllByRole(UUID id, Pageable pageable);
}
