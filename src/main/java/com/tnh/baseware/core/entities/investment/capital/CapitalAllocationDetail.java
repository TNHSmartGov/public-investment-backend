package com.tnh.baseware.core.entities.investment.capital;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tnh.baseware.core.audits.listeners.CapitalAllocationDetailAuditListener;
import com.tnh.baseware.core.entities.audit.Auditable;
import com.tnh.baseware.core.entities.investment.progress.AllocationExecution;
import com.tnh.baseware.core.entities.investment.progress.Disbursement;

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
@EntityListeners(CapitalAllocationDetailAuditListener.class)
public class CapitalAllocationDetail extends Auditable<String> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "capital_allocation_id", nullable = false)
    CapitalAllocation capitalAllocation;

    @OneToMany(mappedBy = "capitalAllocationDetail", cascade = CascadeType.ALL)
    @JsonManagedReference
    @Builder.Default
    Set<AllocationExecution> allocationExecutions = new HashSet<>();

    @OneToMany(mappedBy = "capitalAllocationDetail", cascade = CascadeType.ALL)
    @JsonManagedReference
    @Builder.Default
    Set<Disbursement> disbursements = new HashSet<>();

    Date allocationDate;

    String description;

    @Column(name = "is_approved")
    @Builder.Default
    Boolean isApproved = false;

}
