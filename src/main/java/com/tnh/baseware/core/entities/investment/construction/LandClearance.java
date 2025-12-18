package com.tnh.baseware.core.entities.investment.construction;

import com.tnh.baseware.core.entities.audit.Auditable;
import com.tnh.baseware.core.entities.doc.FileDocument;
import com.tnh.baseware.core.entities.investment.Project;

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
public class LandClearance extends Auditable<String> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    Date implementationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    Project project;

    @ManyToOne
    @JoinColumn(name = "file_document_id", nullable = false, unique = false)
    FileDocument fileDocument;

    String summary;
    
    BigDecimal implementationAmount;

    String description;

}
