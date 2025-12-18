package com.tnh.baseware.core.resources.notification;

import com.tnh.baseware.core.dtos.notification.NotificationDTO;
import com.tnh.baseware.core.dtos.user.ApiMessageDTO;
import com.tnh.baseware.core.entities.notification.Notification;
import com.tnh.baseware.core.forms.notification.NotificationEditorForm;
import com.tnh.baseware.core.properties.SystemProperties;
import com.tnh.baseware.core.resources.GenericResource;
import com.tnh.baseware.core.services.IGenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.notification.INotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${baseware.core.system.api-prefix}/notifications")
@Tag(name = "Notification API", description = "API for managing notifications")
public class NotificationResource extends
        GenericResource<Notification, NotificationEditorForm, NotificationDTO, UUID> {

    INotificationService notificationService;

    public NotificationResource(IGenericService<Notification,NotificationEditorForm, NotificationDTO, UUID> service,
                           MessageService messageService, INotificationService notificationService,
                           SystemProperties systemProperties) {
        super(service, messageService, systemProperties.getApiPrefix() + "/notifications");
        this.notificationService = notificationService;
    }

    @Operation(summary = "Create a new notification")
    @PostMapping
    @Override
    public ResponseEntity<ApiMessageDTO<NotificationDTO>> create(
            @RequestBody NotificationEditorForm form) {
        NotificationDTO result = notificationService.create(form);
        return ResponseEntity.ok(ApiMessageDTO.<NotificationDTO>builder()
                .data(result)
                .result(true)
                .message(messageService.getMessage("notification.created"))
                .code(HttpStatus.OK.value())
                .build());
    }

    @Operation(summary = "Get notifications for an investor")
    @GetMapping("/investor/{investorId}")
    public ResponseEntity<ApiMessageDTO<List<NotificationDTO>>> getInvestorNotifications(
            @PathVariable UUID investorId) {
        List<NotificationDTO> notifications = notificationService.getNotificationsForInvestor(investorId);
        return ResponseEntity.ok(ApiMessageDTO.<List<NotificationDTO>>builder()
                .data(notifications)
                .result(true)
                .message(messageService.getMessage("notification.retrieved"))
                .code(HttpStatus.OK.value())
                .build());
    }

    @Operation(summary = "Mark notification as read")
    @PostMapping("/{id}/read/{investorId}")
    public ResponseEntity<ApiMessageDTO<Void>> markAsRead(
            @PathVariable UUID id, @PathVariable UUID investorId) {
        notificationService.markAsRead(id, investorId);
        return ResponseEntity.ok(ApiMessageDTO.<Void>builder()
                .result(true)
                .message(messageService.getMessage("notification.read"))
                .code(HttpStatus.OK.value())
                .build());
    }
}
