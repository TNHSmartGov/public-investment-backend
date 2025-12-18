package com.tnh.baseware.core.services.audit;

import com.tnh.baseware.core.dtos.audit.EntityMetadataDTO;
import com.tnh.baseware.core.entities.audit.EntityMetadata;
import com.tnh.baseware.core.forms.audit.EntityMetadataForm;
import com.tnh.baseware.core.services.IGenericService;

import java.util.List;
import java.util.UUID;

public interface IEntityMetadataService
        extends IGenericService<EntityMetadata, EntityMetadataForm, EntityMetadataDTO, UUID> {
    List<EntityMetadataDTO> syncAllScanableEntities();
}
