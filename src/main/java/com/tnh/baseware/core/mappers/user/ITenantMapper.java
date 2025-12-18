package com.tnh.baseware.core.mappers.user;

import com.tnh.baseware.core.dtos.user.TenantDTO;
import com.tnh.baseware.core.entities.user.Tenant;
import com.tnh.baseware.core.forms.user.TenantEditorForm;
import com.tnh.baseware.core.mappers.IGenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ITenantMapper extends IGenericMapper<Tenant, TenantEditorForm, TenantDTO> {
}
