package com.tnh.baseware.core.mappers.user;

import com.tnh.baseware.core.dtos.user.PrivilegeDTO;
import com.tnh.baseware.core.entities.user.Privilege;
import com.tnh.baseware.core.forms.user.PrivilegeEditorForm;
import com.tnh.baseware.core.mappers.IGenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IPrivilegeMapper extends IGenericMapper<Privilege, PrivilegeEditorForm, PrivilegeDTO> {
}
