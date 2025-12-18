package com.tnh.baseware.core.entities.investment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.tnh.baseware.core.entities.audit.Auditable;

import lombok.experimental.FieldDefaults;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvestmentPlan extends Auditable<String> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    Project project;

    String name;

    String description;

    @Column(name = "phase_order")
    Integer phaseOrder;

    @Column(name = "start_date")
    LocalDate startDate;

    @Column(name = "end_date")
    LocalDate endDate;

    @Column(name = "estimated_budget")
    BigDecimal estimatedBudget;

    @Column(name = "actual_budget")
    BigDecimal actualBudget;

    String status;

    @Column(name = "is_approved")
    Boolean isApproved;

}
