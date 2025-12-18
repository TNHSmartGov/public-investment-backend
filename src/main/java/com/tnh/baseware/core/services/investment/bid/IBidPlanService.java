package com.tnh.baseware.core.services.investment.bid;

import java.util.UUID;

import com.tnh.baseware.core.dtos.investment.bid.BidPlanDTO;
import com.tnh.baseware.core.entities.investment.bid.BidPlan;
import com.tnh.baseware.core.forms.investment.bid.BidPlanEditorForm;
import com.tnh.baseware.core.services.IGenericService;

public interface IBidPlanService extends
        IGenericService<BidPlan, BidPlanEditorForm, BidPlanDTO, UUID> {

}
