package com.tnh.baseware.core.entities.investment.bid;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import com.tnh.baseware.core.entities.adu.Organization;
import com.tnh.baseware.core.entities.audit.Auditable;
import com.tnh.baseware.core.entities.doc.FileDocument;
import com.tnh.baseware.core.entities.investment.Project;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BidPlan extends Auditable<String> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String numberNo;

    Date signDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    Project project;

    @ManyToOne
    @JoinColumn(name = "authority_org_id", nullable = false)
    Organization authorityOrg;

    @ManyToOne
    @JoinColumn(name = "file_document_id", nullable = false)
    FileDocument fileDocument;

    BigDecimal totalProjectAmount;

    BigDecimal totalImplementedAmount;

    BigDecimal totalBidPackageAmount;

    String description;
}
