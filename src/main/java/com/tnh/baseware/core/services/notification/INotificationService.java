package com.tnh.baseware.core.services.notification;

import com.tnh.baseware.core.dtos.notification.NotificationDTO;
import com.tnh.baseware.core.forms.notification.NotificationEditorForm;

import java.util.List;
import java.util.UUID;

public interface INotificationService {
    NotificationDTO create(NotificationEditorForm form);
    List<NotificationDTO> getNotificationsForInvestor(UUID investorId);
    void markAsRead(UUID id, UUID investorId);
    void delete(UUID id);
    NotificationDTO update(UUID id, NotificationEditorForm form);
    NotificationDTO findById(UUID id);
}
