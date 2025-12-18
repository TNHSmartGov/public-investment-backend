package com.tnh.baseware.core.entities.investment;

import com.tnh.baseware.core.annotations.ScanableEntity;
import com.tnh.baseware.core.entities.adu.Organization;
import com.tnh.baseware.core.entities.audit.Auditable;
import com.tnh.baseware.core.entities.doc.FileDocument;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@ScanableEntity(name = "OtherDecision", alias = "OtherDecision", description = "Represents an Other Decision")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OtherDecision extends Auditable<String> implements Serializable {

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
    @JoinColumn(name = "authority_org_id", nullable = false)
    Organization authorityOrg;

    @ManyToOne
    @JoinColumn(name = "file_document_id", nullable = false)
    FileDocument fileDocument;

    String summary;

    String description;

}
