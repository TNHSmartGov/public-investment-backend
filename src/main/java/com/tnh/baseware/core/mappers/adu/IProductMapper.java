package com.tnh.baseware.core.mappers.adu;

import com.tnh.baseware.core.dtos.adu.ProductDTO;
import com.tnh.baseware.core.entities.adu.Product;
import com.tnh.baseware.core.forms.adu.ProductEditorForm;
import com.tnh.baseware.core.mappers.IGenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IProductMapper extends IGenericMapper<Product, ProductEditorForm, ProductDTO> {
}
