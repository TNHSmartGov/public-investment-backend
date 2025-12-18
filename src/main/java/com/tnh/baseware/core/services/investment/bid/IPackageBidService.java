package com.tnh.baseware.core.services.investment.bid;

import com.tnh.baseware.core.entities.investment.bid.PackageBid;
import com.tnh.baseware.core.dtos.investment.bid.PackageBidDTO;
import com.tnh.baseware.core.forms.investment.bid.PackageBidEditorForm;
import com.tnh.baseware.core.services.IGenericService;

import java.util.UUID;

public interface IPackageBidService extends
        IGenericService<PackageBid, PackageBidEditorForm, PackageBidDTO, UUID> {

}
