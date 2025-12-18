package com.tnh.baseware.core.services.storage.impl;

import com.tnh.baseware.core.components.TenantContext;
import com.tnh.baseware.core.exceptions.BWCGenericRuntimeException;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.storage.IStorageService;
import io.minio.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service("s3StorageService")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class S3StorageService implements IStorageService<String> {

    String bucketPrefix;

    MinioClient minioClient;
    MessageService messageService;

    public S3StorageService(@Value("${minio.bucket-prefix:publicinv-}") String bucketPrefix, MinioClient minioClient, MessageService messageService) {
        this.bucketPrefix = bucketPrefix;
        this.minioClient = minioClient;
        this.messageService = messageService;
    }

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            String bucketName = initTenantBucket();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String path = LocalDate.now().format(formatter) + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(path)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return path;
        } catch (Exception e) {
            throw new BWCGenericRuntimeException(messageService.getMessage("file.upload.error", TenantContext.getTenantId()));
        }
    }

    @Override
    public InputStream downloadFile(String path) {
        try {
            String bucketName = initTenantBucket();
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(path)
                            .build()
            );
        } catch (Exception e) {
            throw new BWCGenericRuntimeException(messageService.getMessage("file.download.error", TenantContext.getTenantId()));
        }
    }

    @Override
    public void deleteFile(String path) {
        try {
            String bucketName = initTenantBucket();
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(path)
                            .build()
            );
        } catch (Exception e) {
            throw new BWCGenericRuntimeException(messageService.getMessage("file.delete.error", TenantContext.getTenantId()));
        }
    }

    private String initTenantBucket() {
        String tenantId = TenantContext.getTenantId();
        String tenantBucket = bucketPrefix + tenantId;
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(tenantBucket).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(tenantBucket).build());
            }
        } catch (Exception e) {
            throw new BWCGenericRuntimeException("Init bucket failed for tenant: " + tenantId);
        }
        return tenantBucket;
    }
}
