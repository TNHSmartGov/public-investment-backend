package com.tnh.baseware.core.services.adu;

import com.tnh.baseware.core.dtos.adu.ProductDTO;
import com.tnh.baseware.core.entities.adu.Product;
import com.tnh.baseware.core.forms.adu.ProductEditorForm;
import com.tnh.baseware.core.services.IGenericService;

import java.util.UUID;

public interface IProductService extends
        IGenericService<Product, ProductEditorForm, ProductDTO, UUID> {
}
