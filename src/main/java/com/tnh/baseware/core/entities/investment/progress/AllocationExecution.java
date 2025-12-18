package com.tnh.baseware.core.entities.investment.progress;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tnh.baseware.core.audits.listeners.AllocationExecutionAuditListener;
import com.tnh.baseware.core.entities.audit.Auditable;
import com.tnh.baseware.core.entities.investment.capital.CapitalAllocationDetail;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AllocationExecutionAuditListener.class)
public class AllocationExecution extends Auditable<String> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String investmentItem;

    String code;

    BigDecimal amount;

    Date executionDate;

    String responsiblePerson;

    Boolean isApproved;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    AllocationExecution parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "capital_allocation_detail_id", nullable = false)
    CapitalAllocationDetail capitalAllocationDetail;

    String description;

}
