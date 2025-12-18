package com.tnh.baseware.core.services.adu.imp;

import com.tnh.baseware.core.dtos.adu.ProductDTO;
import com.tnh.baseware.core.entities.adu.Product;
import com.tnh.baseware.core.forms.adu.ProductEditorForm;
import com.tnh.baseware.core.mappers.adu.IProductMapper;
import com.tnh.baseware.core.repositories.adu.IProductRepository;
import com.tnh.baseware.core.services.GenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.adu.IProductService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProductService extends
        GenericService<Product, ProductEditorForm, ProductDTO, IProductRepository, IProductMapper, UUID>
        implements
        IProductService {

    public ProductService(IProductRepository repository,
                          IProductMapper mapper,
                          MessageService messageService) {
        super(repository, mapper, messageService, Product.class);
    }
}
