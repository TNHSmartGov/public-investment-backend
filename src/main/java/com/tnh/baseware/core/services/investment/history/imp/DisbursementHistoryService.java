package com.tnh.baseware.core.services.investment.history.imp;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.tnh.baseware.core.dtos.investment.history.DisbursementHistoryDTO;
import com.tnh.baseware.core.entities.investment.history.DisbursementHistory;
import com.tnh.baseware.core.forms.investment.history.DisbursementHistoryEditorForm;
import com.tnh.baseware.core.mappers.investment.history.IDisbursementHistoryMapper;
import com.tnh.baseware.core.repositories.investment.history.IDisbursementHistoryRepository;
import com.tnh.baseware.core.services.GenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.investment.history.IDisbursementHistoryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DisbursementHistoryService extends
        GenericService<DisbursementHistory, DisbursementHistoryEditorForm, DisbursementHistoryDTO, IDisbursementHistoryRepository, IDisbursementHistoryMapper, UUID>
        implements IDisbursementHistoryService {

    public DisbursementHistoryService(IDisbursementHistoryRepository repository,
            IDisbursementHistoryMapper mapper, MessageService messageService) {
        super(repository, mapper, messageService, DisbursementHistory.class);
    }

}
