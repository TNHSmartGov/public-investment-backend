package com.tnh.baseware.core.mappers.user;

import com.tnh.baseware.core.dtos.user.RoleDTO;
import com.tnh.baseware.core.entities.user.Role;
import com.tnh.baseware.core.forms.user.RoleEditorForm;
import com.tnh.baseware.core.mappers.IGenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IRoleMapper extends IGenericMapper<Role, RoleEditorForm, RoleDTO> {
}
