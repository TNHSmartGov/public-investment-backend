package com.tnh.baseware.core.resources.adu;

import com.tnh.baseware.core.dtos.adu.ProductDTO;
import com.tnh.baseware.core.entities.adu.Product;
import com.tnh.baseware.core.forms.adu.ProductEditorForm;
import com.tnh.baseware.core.properties.SystemProperties;
import com.tnh.baseware.core.resources.GenericResource;
import com.tnh.baseware.core.services.IGenericService;
import com.tnh.baseware.core.services.MessageService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Products", description = "API for managing products")
@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${baseware.core.system.api-prefix}/products")
public class ProductResource extends
        GenericResource<Product, ProductEditorForm, ProductDTO, UUID> {

    public ProductResource(IGenericService<Product, ProductEditorForm, ProductDTO, UUID> service,
            MessageService messageService,
            SystemProperties systemProperties) {
        super(service, messageService, systemProperties.getApiPrefix() + "/products");
    }
}
