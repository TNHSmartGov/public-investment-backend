package com.tnh.baseware.core.entities.investment.capital;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tnh.baseware.core.entities.audit.Auditable;
import com.tnh.baseware.core.entities.investment.progress.AllocationExecution;
import com.tnh.baseware.core.entities.investment.progress.Disbursement;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CapitalPlanLine extends Auditable<String> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String code;

    BigDecimal amount;

    Integer year;

    String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "capital_plan_id", nullable = false)
    CapitalPlan capitalPlan;    

    @OneToMany(mappedBy = "capitalPlanLine", cascade = CascadeType.ALL)
    Set<Disbursement> disbursements = new HashSet<>();

    @OneToMany(mappedBy = "capitalPlanLine", cascade = CascadeType.ALL)
    Set<AllocationExecution> allocationExecutions = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_allocation_id", nullable = false)
    ProjectCapitalAllocation projectCapitalAllocation;

    //Tính tổng lũy kế số tiền đã thực hiện/giải ngân trong năm
    public BigDecimal getTotalAllocationExecution() {
        if (this.allocationExecutions == null || this.allocationExecutions.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        return this.allocationExecutions.stream()
            .map(exec -> exec.getAmount() != null ? exec.getAmount() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    //Tính tổng số tiền đã giải ngân thực tế trong năm
    public BigDecimal getTotalDisbursed() {
        if (disbursements == null || disbursements.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return disbursements.stream()
            .map(disb -> disb.getAmount() != null ? disb.getAmount() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    //Tính số dư vốn còn lại có thể giải ngân
    public BigDecimal getRemainingAmount() {
        return amount.subtract(getTotalDisbursed());
    }
}