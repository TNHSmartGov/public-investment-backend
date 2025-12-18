package com.tnh.baseware.core.repositories.notification;

import com.tnh.baseware.core.entities.adu.Organization;
import com.tnh.baseware.core.entities.notification.Notification;
import com.tnh.baseware.core.repositories.IGenericRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface INotificationRepository extends IGenericRepository<Notification, UUID> {
    List<Notification> findByRecipientsContainingAndExpirationDateAfter(Organization recipient, Date date);
    List<Notification> findByRecipientsContaining(Organization recipient);
}
