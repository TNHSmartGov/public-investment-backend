package com.tnh.baseware.core.dtos.notification;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tnh.baseware.core.dtos.doc.FileDocumentDTO;
import com.tnh.baseware.core.dtos.investment.OtherDecisionDTO;
import com.tnh.baseware.core.entities.audit.Identifiable;
import com.tnh.baseware.core.enums.NotificationStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationDTO extends RepresentationModel<NotificationDTO> implements Identifiable<UUID> {
    UUID id;
    String title;
    String summary;
    String content;
    Date expirationDate;
    UUID senderId;
    Set<NotificationReceiptDTO> receipts = new HashSet<>();
    NotificationStatus status;
    Date createdDate;
    Date lastModifiedDate;
    FileDocumentDTO fileDocument;
    boolean isRead;

    public boolean isReadByRecipient(UUID recipientId) {
        return receipts.stream()
                .filter(receipt -> receipt.getRecipientId().equals(recipientId))
                .findFirst()
                .map(NotificationReceiptDTO::isRead)
                .orElse(false);
    }
}
