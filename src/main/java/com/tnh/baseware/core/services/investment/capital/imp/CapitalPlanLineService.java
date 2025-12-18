package com.tnh.baseware.core.services.investment.capital.imp;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.dtos.investment.capital.CapitalPlanLineDTO;
import com.tnh.baseware.core.entities.investment.capital.CapitalPlan;
import com.tnh.baseware.core.entities.investment.capital.CapitalPlanLine;
import com.tnh.baseware.core.exceptions.BWCNotFoundException;
import com.tnh.baseware.core.exceptions.BWCValidationException;
import com.tnh.baseware.core.forms.investment.CapitalPlanLineEditorForm;
import com.tnh.baseware.core.mappers.investment.capital.ICapitalPlanLineMapper;
import com.tnh.baseware.core.repositories.investment.capital.ICapitalPlanLineRepository;
import com.tnh.baseware.core.repositories.investment.capital.ICapitalPlanRepository;
import com.tnh.baseware.core.services.GenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.investment.capital.ICapitalPlanLineService;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CapitalPlanLineService extends
        GenericService<CapitalPlanLine, CapitalPlanLineEditorForm, CapitalPlanLineDTO, ICapitalPlanLineRepository, ICapitalPlanLineMapper, UUID>
         implements
        ICapitalPlanLineService {

        ICapitalPlanRepository capitalPlanRepository;
        GenericEntityFetcher fetcher;
        ICapitalPlanLineMapper mapper;

        public CapitalPlanLineService(ICapitalPlanLineRepository repository,
            ICapitalPlanLineMapper mapper,
            ICapitalPlanRepository capitalPlanRepository,
            MessageService messageService, GenericEntityFetcher fetcher) {
                super(repository, mapper, messageService, CapitalPlanLine.class);
                this.capitalPlanRepository = capitalPlanRepository;
                this.mapper = mapper;
                this.fetcher = fetcher;
        }

        @Override
        @Transactional(isolation = Isolation.READ_COMMITTED)
        public CapitalPlanLineDTO create(CapitalPlanLineEditorForm form) {
                var capitalPlanLine = mapper.formToEntity(form, fetcher, capitalPlanRepository);

                return mapper.entityToDTO(repository.save(capitalPlanLine));
        }

        @Override
        @Transactional(isolation = Isolation.READ_COMMITTED)
        public CapitalPlanLineDTO update(UUID id, CapitalPlanLineEditorForm form) {
                var capitalPlanLine = repository.findById(id)
                        .orElseThrow(() -> new BWCNotFoundException(messageService.getMessage("capitalplanline.not.found", id)));
                mapper.updateCapitalPlanLineFromForm(form, capitalPlanLine, fetcher, capitalPlanRepository);

                BigDecimal totalCapitalPlan = BigDecimal.ZERO;

                List<CapitalPlanLine> capitalPlanLines = repository.findAllByDeletedFalse();

                for (CapitalPlanLine capitalPlanLineItem : capitalPlanLines) {
                        totalCapitalPlan = totalCapitalPlan.add(capitalPlanLineItem.getAmount());
                }

                CapitalPlan capitalPlan = capitalPlanRepository.findById(form.getCapitalPlanId())
                        .orElseThrow(() -> new BWCNotFoundException(messageService.getMessage("capitalplan.not.found", form.getCapitalPlanId())));

                BigDecimal current = capitalPlan.getTotalAmountPlan();
                BigDecimal newTotal = totalCapitalPlan.add(form.getAmount()).subtract(capitalPlanLine.getAmount());

                if (current.compareTo(newTotal) < 0) {
                throw new BWCValidationException(messageService.getMessage("capital.planline.over"));
                }
                
                return mapper.entityToDTO(repository.save(capitalPlanLine));
        }

}
