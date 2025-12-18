package com.tnh.baseware.core.entities.notification;

import com.tnh.baseware.core.annotations.ScanableEntity;
import com.tnh.baseware.core.entities.adu.Organization;
import com.tnh.baseware.core.entities.audit.Auditable;
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
@ScanableEntity(name = "NotificationReceipt", alias = "notificationReceipt")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationReceipt extends Auditable<String> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne
    @JoinColumn(name = "notification_id", nullable = false)
    Notification notification;

    @ManyToOne
    @JoinColumn(name = "recipient_org_id", nullable = false)
    Organization recipient;

    @Column(name = "read_date")
    Date readDate;

    @Column(nullable = false)
    boolean isRead;
}
