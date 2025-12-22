package com.tnh.baseware.core.resources.investment.history;

import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tnh.baseware.core.dtos.investment.history.DisbursementHistoryDTO;
import com.tnh.baseware.core.entities.investment.history.DisbursementHistory;
import com.tnh.baseware.core.forms.investment.history.DisbursementHistoryEditorForm;
import com.tnh.baseware.core.properties.SystemProperties;
import com.tnh.baseware.core.resources.GenericResource;
import com.tnh.baseware.core.services.IGenericService;
import com.tnh.baseware.core.services.MessageService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Tag(name = "DisbursementHistory", description = "API for viewing disbursement history")
@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${baseware.core.system.api-prefix}/disbursement-histories")
public class DisbursementHistoryResource extends
        GenericResource<DisbursementHistory, DisbursementHistoryEditorForm, DisbursementHistoryDTO, UUID> {

    public DisbursementHistoryResource(
            IGenericService<DisbursementHistory, DisbursementHistoryEditorForm, DisbursementHistoryDTO, UUID> service,
            MessageService messageService, SystemProperties systemProperties) {
        super(service, messageService, systemProperties.getApiPrefix() + "/disbursement-histories");
    }
}
