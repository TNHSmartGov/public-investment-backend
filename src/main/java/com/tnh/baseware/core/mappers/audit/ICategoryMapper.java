package com.tnh.baseware.core.mappers.audit;

import com.tnh.baseware.core.dtos.audit.CategoryDTO;
import com.tnh.baseware.core.entities.audit.Category;
import com.tnh.baseware.core.forms.audit.CategoryEditorForm;
import com.tnh.baseware.core.mappers.IGenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICategoryMapper extends IGenericMapper<Category, CategoryEditorForm, CategoryDTO> {
}
