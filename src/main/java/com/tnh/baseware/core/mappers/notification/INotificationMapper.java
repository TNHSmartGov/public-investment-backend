package com.tnh.baseware.core.mappers.notification;

import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.dtos.notification.NotificationDTO;
import com.tnh.baseware.core.dtos.notification.NotificationReceiptDTO;
import com.tnh.baseware.core.entities.notification.Notification;
import com.tnh.baseware.core.forms.notification.NotificationEditorForm;
import com.tnh.baseware.core.mappers.IGenericMapper;
import com.tnh.baseware.core.mappers.adu.IOrganizationMapper;
import com.tnh.baseware.core.repositories.adu.IOrganizationRepository;
import com.tnh.baseware.core.repositories.doc.IFileDocumentRepository;
import com.tnh.baseware.core.repositories.notification.INotificationReceiptRepository;
import org.mapstruct.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {
                IOrganizationMapper.class
        })
public interface INotificationMapper extends IGenericMapper<Notification, NotificationEditorForm, NotificationDTO> {
    @Mapping(target = "sender", expression = "java(fetcher.formToEntity(organizationRepository, form.getSenderId()))")
    @Mapping(target = "fileDocument", expression = "java(fetcher.formToEntity(fileDocumentRepository, form.getFileDocumentId()))")
    Notification formToEntity(NotificationEditorForm form,
                              @Context GenericEntityFetcher fetcher,
                              @Context IOrganizationRepository organizationRepository,
                              @Context IFileDocumentRepository fileDocumentRepository);

    @Mapping(target = "sender", expression = "java(fetcher.formToEntity(organizationRepository, form.getSenderId()))")
    void updateNotificationFromForm(NotificationEditorForm form, @MappingTarget Notification notification,
                                    @Context GenericEntityFetcher fetcher,
                                    @Context IOrganizationRepository organizationRepository);

    @Mapping(target = "senderId", source = "notification.sender.id")
    @Mapping(target = "receipts", ignore = true)
    NotificationDTO entityToDTO(Notification notification);

    @Override
    void entityToDTO(Notification entity, @MappingTarget NotificationDTO dto);

    List<NotificationDTO> entitiesToDTOs(List<Notification> notifications);

    default NotificationDTO populateReceiptsAndReturn(Notification notification, NotificationDTO dto,
                                                      INotificationReceiptRepository receiptRepository) {
        if (receiptRepository != null && notification.getRecipients() != null) {
            Set<NotificationReceiptDTO> receipts = new HashSet<>();

            notification.getRecipients().forEach(recipient -> {
                NotificationReceiptDTO receiptDTO = new NotificationReceiptDTO();
                receiptDTO.setRecipientId(recipient.getId());

                receiptRepository.findByNotificationIdAndRecipientId(notification.getId(), recipient.getId())
                        .ifPresent(receipt -> {
                            receiptDTO.setRead(receipt.isRead());
                            receiptDTO.setReadDate(receipt.getReadDate());
                        });

                receipts.add(receiptDTO);
            });

            dto.setReceipts(receipts);
        }
        return dto;
    }
}
