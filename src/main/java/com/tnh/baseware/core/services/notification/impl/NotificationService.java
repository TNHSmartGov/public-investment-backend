package com.tnh.baseware.core.services.notification.impl;

import com.tnh.baseware.core.components.EnumRegistry;
import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.dtos.notification.NotificationDTO;
import com.tnh.baseware.core.dtos.notification.NotificationReceiptDTO;
import com.tnh.baseware.core.entities.adu.Organization;
import com.tnh.baseware.core.entities.notification.Notification;
import com.tnh.baseware.core.entities.notification.NotificationReceipt;
import com.tnh.baseware.core.exceptions.BWCGenericRuntimeException;
import com.tnh.baseware.core.forms.notification.NotificationEditorForm;
import com.tnh.baseware.core.mappers.notification.INotificationMapper;
import com.tnh.baseware.core.repositories.adu.IOrganizationRepository;
import com.tnh.baseware.core.repositories.doc.IFileDocumentRepository;
import com.tnh.baseware.core.repositories.notification.INotificationReceiptRepository;
import com.tnh.baseware.core.repositories.notification.INotificationRepository;
import com.tnh.baseware.core.services.GenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.notification.INotificationService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class NotificationService extends
        GenericService<Notification, NotificationEditorForm, NotificationDTO,
                INotificationRepository, INotificationMapper, UUID>
        implements INotificationService {

    INotificationReceiptRepository notificationReceiptRepository;
    IOrganizationRepository organizationRepository;
    IFileDocumentRepository fileDocumentRepository;
    GenericEntityFetcher fetcher;

    public NotificationService(INotificationRepository repository,
                               INotificationMapper mapper,
                               MessageService messageService,
                               EnumRegistry enumRegistry,
                               IOrganizationRepository organizationRepository,
                               GenericEntityFetcher fetcher,
                               INotificationReceiptRepository notificationReceiptRepository,
                               IFileDocumentRepository fileDocumentRepository) {
        super(repository, mapper, messageService, Notification.class, enumRegistry);
        this.organizationRepository = organizationRepository;
        this.notificationReceiptRepository = notificationReceiptRepository;
        this.fetcher = fetcher;
        this.fileDocumentRepository = fileDocumentRepository;
    }

    @Override
    public NotificationDTO findById(UUID id) {
        var notification = repository.findById(id)
                .orElseThrow(() -> notFound("notification.not.found"));
        var dto = mapper.entityToDTO(notification);
        return mapper.populateReceiptsAndReturn(notification, dto, notificationReceiptRepository);
    }

    @Override
    @Transactional
    public NotificationDTO create(NotificationEditorForm form) {
        validateExpirationDate(form.getExpirationDate());

        var notification = mapper.formToEntity(form, fetcher, organizationRepository, fileDocumentRepository);

        var savedNotification = repository.save(notification);
        handleRecipients(savedNotification, form.getRecipientIds(), false);

        log.debug("Created notification {} with {} recipients", savedNotification.getId(),
                savedNotification.getRecipients().size());

        var dto = mapper.entityToDTO(savedNotification);
        return mapper.populateReceiptsAndReturn(savedNotification, dto, notificationReceiptRepository);
    }

    @Override
    @Transactional
    public NotificationDTO update(UUID id, NotificationEditorForm form) {
        Notification notification = repository.findById(id)
                .orElseThrow(() -> notFound("notification.not.found"));

        validateExpirationDate(form.getExpirationDate());

        Notification updatedNotification = mapFormToEntity(notification, form);
        Notification savedNotification = repository.save(updatedNotification);

        if (form.getRecipientIds() != null) {
            handleRecipients(savedNotification, form.getRecipientIds(), true);
        }

        log.debug("Updated notification {}", savedNotification.getId());
        var dto = mapper.entityToDTO(savedNotification);
        return mapper.populateReceiptsAndReturn(savedNotification, dto, notificationReceiptRepository);
    }

    @Override
    public List<NotificationDTO> getNotificationsForInvestor(UUID investorId) {
        Organization investor = organizationRepository.findById(investorId)
                .orElseThrow(() -> notFound("organization.not.found"));

        List<Notification> notifications = repository.findByRecipientsContainingAndExpirationDateAfter(investor, new Date());

        return notifications.stream()
                .map(n -> {
                    var dto = mapper.entityToDTO(n);
                    notificationReceiptRepository.findByNotificationIdAndRecipientId(n.getId(), investorId)
                            .ifPresent(receipt -> dto.setRead(receipt.isRead()));
                    return dto;
                })
                .sorted(Comparator.comparing(NotificationDTO::getCreatedDate).reversed())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markAsRead(UUID id, UUID investorId) {
        NotificationReceipt receipt = notificationReceiptRepository
                .findByNotificationIdAndRecipientId(id, investorId)
                .orElseThrow(() -> notFound("notification.receipt.not.found"));

        if (!receipt.isRead()) {
            receipt.setRead(true);
            receipt.setReadDate(new Date());
            notificationReceiptRepository.save(receipt);
            log.debug("Marked notification {} as read for investor {}", id, investorId);
        }
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        notificationReceiptRepository.deleteByNotificationId(id);
        repository.deleteById(id);
        log.debug("Deleted notification {}", id);
    }

    // ---------------- PRIVATE HELPERS ----------------

    private void validateExpirationDate(Date expirationDate) {
        if (expirationDate != null && expirationDate.before(new Date())) {
            throw new BWCGenericRuntimeException(
                    messageService.getMessage("notification.expiration.invalid"));
        }
    }

    private Notification mapFormToEntity(Notification notification, NotificationEditorForm form) {
        notification.setTitle(form.getTitle());
        notification.setSummary(form.getSummary());
        notification.setContent(form.getContent());
        notification.setExpirationDate(form.getExpirationDate());

        if (form.getSenderId() != null) {
            Organization sender = organizationRepository.findById(form.getSenderId())
                    .orElseThrow(() -> notFound("organization.not.found"));
            notification.setSender(sender);
        }
        return notification;
    }

    private void handleRecipients(Notification notification, Set<UUID> recipientIds, boolean clearOld) {
        Set<Organization> recipients = loadRecipients(recipientIds);
        notification.setRecipients(recipients);

        if (clearOld) {
            notificationReceiptRepository.deleteByNotificationId(notification.getId());
        }

        recipients.forEach(recipient -> {
            NotificationReceipt receipt = new NotificationReceipt();
            receipt.setNotification(notification);
            receipt.setRecipient(recipient);
            receipt.setRead(false);
            notificationReceiptRepository.save(receipt);
        });
    }

    private Set<Organization> loadRecipients(Set<UUID> recipientIds) {
        if (recipientIds == null || recipientIds.isEmpty()) {
            return Collections.emptySet();
        }

        List<Organization> recipients = organizationRepository.findAllById(recipientIds);
        if (recipients.size() != recipientIds.size()) {
            throw notFound("organization.not.found");
        }
        return new HashSet<>(recipients);
    }

    private BWCGenericRuntimeException notFound(String key) {
        return new BWCGenericRuntimeException(messageService.getMessage(key));
    }
}
