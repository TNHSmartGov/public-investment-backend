package com.tnh.baseware.core.services.investment.imp;

import java.util.UUID;

import com.tnh.baseware.core.dtos.investment.InvestmentPlanDTO;
import com.tnh.baseware.core.entities.investment.InvestmentPlan;
import com.tnh.baseware.core.entities.investment.Project;
import com.tnh.baseware.core.exceptions.BWCNotFoundException;
import com.tnh.baseware.core.forms.investment.InvestmentPlanEditorForm;
import com.tnh.baseware.core.mappers.investment.IInvestmentPlanMapper;
import com.tnh.baseware.core.repositories.investment.IInvestmentPlanRepository;
import com.tnh.baseware.core.services.GenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.investment.IInvestmentPlanService;

public class InvestmentPlanService extends GenericService<InvestmentPlan, InvestmentPlanEditorForm
            , InvestmentPlanDTO, IInvestmentPlanRepository, IInvestmentPlanMapper, UUID>
    implements
    IInvestmentPlanService {

    IInvestmentPlanRepository repository;
    IInvestmentPlanMapper mapper;
    MessageService messageService;

    public InvestmentPlanService(IInvestmentPlanRepository repository,
                    IInvestmentPlanMapper mapper,
                    MessageService messageService) {
            super(repository, mapper, messageService, InvestmentPlan.class);
            this.repository = repository;
            this.mapper = mapper;
            this.messageService = messageService;
    }

    public boolean approve(UUID id) {
        InvestmentPlan plan = repository.findById(id).orElseThrow(
                                () -> new BWCNotFoundException(messageService.getMessage("investment.plan.not.found", id)));
        validateProjectPhase(plan);
        plan.setStatus("approved");
        repository.save(plan);
        return true;
    }

    public boolean reject(UUID id) {
        InvestmentPlan plan = repository.findById(id).orElseThrow(
                                () -> new BWCNotFoundException(messageService.getMessage("investment.plan.not.found", id)));
        plan.setStatus("rejected");
        repository.save(plan);
        return true;
    }

    private void validateProjectPhase(InvestmentPlan plan) {
        Project project = plan.getProject();

        if (project.getStartPlanDate() == null || project.getEndPlanDate() == null) {
            throw new IllegalArgumentException(messageService.getMessage("start.plan.date.not.found")); //Dự án chưa khai báo thời gian thực hiện
        }

        Integer pharseStartYear = plan.getStartDate().getYear();
        Integer pharseEndYear =  plan.getEndDate().getYear();

        if (pharseStartYear < Integer.parseInt(project.getStartPlanDate()) || pharseEndYear > Integer.parseInt(project.getEndPlanDate())) {
            throw new IllegalArgumentException(
                String.format(messageService.getMessage("pharse.plan.date.not.found") + " (%d-%d)",  //Kế hoạch vốn (%d-%d) không nằm trong giai đoạn dự án (%d-%d)
                        Integer.parseInt(project.getStartPlanDate()), Integer.parseInt(project.getEndPlanDate())));
        }
    }

}
