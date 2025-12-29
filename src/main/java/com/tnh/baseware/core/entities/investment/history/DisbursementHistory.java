package com.tnh.baseware.core.entities.investment.history;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.tnh.baseware.core.entities.audit.Auditable;
import com.tnh.baseware.core.entities.investment.Project;
import com.tnh.baseware.core.entities.investment.capital.CapitalPlanLine;

import com.tnh.baseware.core.enums.ActionType;

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
public class DisbursementHistory extends Auditable<String> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    // Link to the original record
    @Column(name = "original_id", nullable = false)
    UUID originalId;

    @Column(name = "action_type")
    @Enumerated(EnumType.STRING)
    ActionType actionType;

    // Snapshot fields
    String investmentItem;
    String code;
    BigDecimal amount;
    Instant disbursementDate;
    String responsiblePerson;
    String voucherNumber;
    Boolean isApproved;
    
    @Column(name = "disbursement_type")
    String disbursementType;
    
    String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_line_id")
    CapitalPlanLine capitalPlanLine;

}
