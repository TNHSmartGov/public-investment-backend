package com.tnh.baseware.core.entities.investment.history;

import com.tnh.baseware.core.entities.audit.Auditable;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

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

    UUID capitalAllocationDetailId;

    BigDecimal amount;

    Date executionDate;

    String action;

    String responsiblePerson;

    String description;

}
