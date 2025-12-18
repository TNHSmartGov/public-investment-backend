package com.tnh.baseware.core.services.audit.imp;

import com.tnh.baseware.core.components.EnumRegistry;
import com.tnh.baseware.core.dtos.audit.CategoryDTO;
import com.tnh.baseware.core.entities.audit.Category;
import com.tnh.baseware.core.forms.audit.CategoryEditorForm;
import com.tnh.baseware.core.mappers.audit.ICategoryMapper;
import com.tnh.baseware.core.repositories.audit.ICategoryRepository;
import com.tnh.baseware.core.services.GenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.audit.ICategoryService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CategoryService extends
        GenericService<Category, CategoryEditorForm, CategoryDTO, ICategoryRepository, ICategoryMapper, UUID>
        implements
        ICategoryService {

    public CategoryService(ICategoryRepository repository,
            ICategoryMapper mapper,
            MessageService messageService,
            EnumRegistry enumRegistry) {
        super(repository, mapper, messageService, Category.class, enumRegistry);
    }
}
