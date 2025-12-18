package com.tnh.baseware.core.entities.investment.history;

import com.tnh.baseware.core.entities.audit.Auditable;
import com.tnh.baseware.core.annotations.ScanableEntity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ScanableEntity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CapitalAllocationDetailHistory extends Auditable<String> implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    UUID capitalAllocationId;

    UUID capitalAllocationDetailId;

    BigDecimal amount;

    String action;

    String description;

}
