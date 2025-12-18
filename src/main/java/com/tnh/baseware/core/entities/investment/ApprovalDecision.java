package com.tnh.baseware.core.entities.investment;

import com.tnh.baseware.core.entities.adu.Organization;
import com.tnh.baseware.core.entities.audit.Auditable;
import com.tnh.baseware.core.entities.doc.FileDocument;

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
public class ApprovalDecision extends Auditable<String> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false)
    String code;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String type;

    String numberNo;

    Date signDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    Project project;

    @ManyToOne
    @JoinColumn(name = "authority_org_id", nullable = false, unique = false)
    Organization authorityOrg;

    @ManyToOne
    @JoinColumn(name = "file_document_id", nullable = false, unique = false)
    FileDocument fileDocument;

    String summary;
    
    Date startDate;
    Date endDate;

    BigDecimal totalRegistrationAmount;
    BigDecimal totalAppraisalAmount;
    BigDecimal totalApprovalAmount;

    String description;

}
