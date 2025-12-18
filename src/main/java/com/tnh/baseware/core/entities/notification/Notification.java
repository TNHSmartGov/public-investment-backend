package com.tnh.baseware.core.entities.notification;

import com.tnh.baseware.core.annotations.ScanableEntity;
import com.tnh.baseware.core.entities.adu.Organization;
import com.tnh.baseware.core.entities.audit.Auditable;
import com.tnh.baseware.core.entities.doc.FileDocument;
import com.tnh.baseware.core.enums.NotificationStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@ScanableEntity(name = "Notification", alias = "notification", description = "Represents a notification from planning department to investors")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification extends Auditable<String> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    String summary;

    @Column(nullable = false, length = 4000)
    String content;

    @Column(name = "expiration_date", nullable = false)
    Date expirationDate;

    @ManyToOne
    @JoinColumn(name = "sender_org_id", nullable = false)
    Organization sender;

    @ManyToMany
    @JoinTable(
            name = "notification_recipient",
            joinColumns = @JoinColumn(name = "notification_id"),
            inverseJoinColumns = @JoinColumn(name = "organization_id")
    )
    Set<Organization> recipients = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    NotificationStatus status;

    @ManyToOne
    @JoinColumn(name = "file_document_id", nullable = false)
    FileDocument fileDocument;
}
