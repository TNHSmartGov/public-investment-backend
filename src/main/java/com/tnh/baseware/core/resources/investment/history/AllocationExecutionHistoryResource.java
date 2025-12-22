package com.tnh.baseware.core.resources.investment.history;

import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tnh.baseware.core.dtos.investment.history.AllocationExecutionHistoryDTO;
import com.tnh.baseware.core.entities.investment.history.AllocationExecutionHistory;
import com.tnh.baseware.core.forms.investment.history.AllocationExecutionHistoryEditorForm;
import com.tnh.baseware.core.properties.SystemProperties;
import com.tnh.baseware.core.resources.GenericResource;
import com.tnh.baseware.core.services.IGenericService;
import com.tnh.baseware.core.services.MessageService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Tag(name = "AllocationExecutionHistory", description = "API for viewing allocation execution history")
@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${baseware.core.system.api-prefix}/allocation-execution-histories")
public class AllocationExecutionHistoryResource extends
        GenericResource<AllocationExecutionHistory, AllocationExecutionHistoryEditorForm, AllocationExecutionHistoryDTO, UUID> {

    public AllocationExecutionHistoryResource(
            IGenericService<AllocationExecutionHistory, AllocationExecutionHistoryEditorForm, AllocationExecutionHistoryDTO, UUID> service,
            MessageService messageService, SystemProperties systemProperties) {
        super(service, messageService, systemProperties.getApiPrefix() + "/allocation-execution-histories");
    }
}
