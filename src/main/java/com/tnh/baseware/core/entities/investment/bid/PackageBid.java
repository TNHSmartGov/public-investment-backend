package com.tnh.baseware.core.entities.investment.bid;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import com.tnh.baseware.core.entities.audit.Auditable;
import com.tnh.baseware.core.entities.audit.Category;
import com.tnh.baseware.core.entities.investment.Project;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PackageBid extends Auditable<String> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    Project project;

    String name;

    BigDecimal packageValue;

    Date biddingTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    Category biddingMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selection_procedure_id", nullable = false)
    Category selectionProcedure;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_type_id", nullable = false)
    Category contractType;

    Integer executionTime;

    String executionTimeUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bidding_field_id", nullable = false)
    Category biddingField;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bidding_form_id", nullable = false)
    Category biddingForm;

    String description;
}
