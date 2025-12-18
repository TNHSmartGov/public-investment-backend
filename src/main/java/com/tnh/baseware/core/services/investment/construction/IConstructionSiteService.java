package com.tnh.baseware.core.services.investment.construction;

import java.util.UUID;

import com.tnh.baseware.core.dtos.investment.construction.ConstructionSiteDTO;
import com.tnh.baseware.core.entities.investment.construction.ConstructionSite;
import com.tnh.baseware.core.forms.investment.construction.ConstructionSiteEditorForm;
import com.tnh.baseware.core.services.IGenericService;

public interface IConstructionSiteService extends
        IGenericService<ConstructionSite, ConstructionSiteEditorForm, ConstructionSiteDTO, UUID> {       

}
