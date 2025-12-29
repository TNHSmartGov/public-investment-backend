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
public class AllocationExecutionHistory extends Auditable<String> implements Serializable {

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
    Instant executionDate;
    String responsiblePerson;
    Boolean isApproved;
    
    @Column(name = "execution_type")
    String executionType;
    String description;

    // Relationships (store ID or reference appropriately, usually reference implies FK constraints which might be strict for history, but here we'll keep loose or same)
    // For history, oftentimes we just store the ID string to avoid Foreign Key issues if the parent is deleted. 
    // BUT given this is a "shadow" table in a relational DB, let's keep it simple: Link to Project/PlanLine if they exist.
    // If requirement is strict audit where parent might be deleted, we store IDs. 
    // Assuming soft-delete system-wide, FKs are fine.
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_line_id")
    CapitalPlanLine capitalPlanLine;

}
