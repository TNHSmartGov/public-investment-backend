package com.tnh.baseware.core.repositories.notification;

import com.tnh.baseware.core.entities.notification.NotificationReceipt;
import com.tnh.baseware.core.repositories.IGenericRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface INotificationReceiptRepository extends IGenericRepository<NotificationReceipt, UUID> {
    Optional<NotificationReceipt> findByNotificationIdAndRecipientId(UUID notificationId, UUID recipientId);
    List<NotificationReceipt> findByNotificationId(UUID notificationId);
    List<NotificationReceipt> findByRecipientId(UUID recipientId);
    void deleteByNotificationId(UUID notificationId);
}
