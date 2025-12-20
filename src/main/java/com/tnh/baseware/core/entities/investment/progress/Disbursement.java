package com.tnh.baseware.core.entities.investment.progress;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.tnh.baseware.core.entities.audit.Auditable;
import com.tnh.baseware.core.entities.investment.Project;
import com.tnh.baseware.core.entities.investment.capital.CapitalPlanLine;

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
public class Disbursement extends Auditable<String> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String investmentItem;

    String code;

    BigDecimal amount;

    Instant disbursementDate;

    String responsiblePerson;

    String voucherNumber; // Số chứng từ

    Boolean isApproved;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    Project project; // Giải ngân cho dự án nào

    @Column(name = "disbursement_type")
    String disbursementType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_line_id", nullable = false)
    CapitalPlanLine capitalPlanLine;

    String description;
}
