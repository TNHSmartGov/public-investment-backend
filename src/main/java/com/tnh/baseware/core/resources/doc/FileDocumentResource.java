package com.tnh.baseware.core.resources.doc;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.tnh.baseware.core.dtos.doc.FileDocumentDTO;
import com.tnh.baseware.core.dtos.doc.FileResource;
import com.tnh.baseware.core.dtos.user.ApiMessageDTO;
import com.tnh.baseware.core.entities.doc.FileDocument;
import com.tnh.baseware.core.exceptions.BWCGenericRuntimeException;
import com.tnh.baseware.core.forms.doc.FileDocumentEditorForm;
import com.tnh.baseware.core.properties.SystemProperties;
import com.tnh.baseware.core.resources.GenericResource;
import com.tnh.baseware.core.services.IGenericService;
import com.tnh.baseware.core.services.MessageService;

import com.tnh.baseware.core.services.doc.imp.FileDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "FileDocuments", description = "API for managing FileDocuments")
@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${baseware.core.system.api-prefix}/file-documents")
@Slf4j
public class FileDocumentResource extends
        GenericResource<FileDocument, FileDocumentEditorForm, FileDocumentDTO, UUID> {

    FileDocumentService fileDocumentService;

    public FileDocumentResource(IGenericService<FileDocument, FileDocumentEditorForm, FileDocumentDTO, UUID> service,
                                MessageService messageService,
                                SystemProperties systemProperties, FileDocumentService fileDocumentService) {
        super(service, messageService, systemProperties.getApiPrefix() + "/file-documents");
        this.fileDocumentService = fileDocumentService;
    }

    @Operation(summary = "Upload a file")
    @ApiResponse(responseCode = "200", description = "Upload file successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
    @PostMapping("/upload")
    public ResponseEntity<ApiMessageDTO<FileDocumentDTO>> uploadFile(@RequestPart("file") MultipartFile multipartFile) {
        FileDocumentDTO file = fileDocumentService.uploadFile(multipartFile);

        ApiMessageDTO<FileDocumentDTO> apiMessageDTO = ApiMessageDTO.<FileDocumentDTO>builder()
                .result(true)
                .data(file)
                .message(messageService.getMessage("file.uploaded"))
                .code(HttpStatus.OK.value())
                .build();

        return ResponseEntity.ok(apiMessageDTO);
    }

    @Operation(summary = "Upload multiple files")
    @ApiResponse(responseCode = "200", description = "Upload files successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
    @PostMapping("/upload-batch")
    public ResponseEntity<ApiMessageDTO<List<FileDocumentDTO>>> uploadFiles(@RequestPart("files") List<MultipartFile> multipartFiles) {
        List<FileDocumentDTO> files = fileDocumentService.uploadFiles(multipartFiles);

        ApiMessageDTO<List<FileDocumentDTO>> apiMessageDTO = ApiMessageDTO.<List<FileDocumentDTO>>builder()
                .result(true)
                .data(files)
                .message(messageService.getMessage("file.uploaded"))
                .code(HttpStatus.OK.value())
                .build();

        return ResponseEntity.ok(apiMessageDTO);
    }

    @Operation(summary = "Download a file")
    @ApiResponse(responseCode = "200", description = "File downloaded successfully")
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@Parameter(description = "File ID") @PathVariable UUID id) {
        try (FileResource resource = fileDocumentService.downloadFile(id)) {
            String contentType = resource.getContentType() != null ?
                    resource.getContentType() : MediaType.APPLICATION_OCTET_STREAM_VALUE;
            String filename = resource.getFileName();

            byte[] fileBytes = IOUtils.toByteArray(resource.getInputStream());

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                    .body(fileBytes);
        } catch (IOException e) {
            throw new BWCGenericRuntimeException(messageService.getMessage("file.download.error", e.getMessage()));
        }
    }

    @Operation(summary = "Download multiple files as ZIP")
    @ApiResponse(responseCode = "200", description = "Files downloaded successfully")
    @PostMapping("/download-batch")
    public void downloadFiles(@RequestBody List<UUID> ids, HttpServletResponse response) throws IOException {
        response.setContentType("application/zip");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"files.zip\"");

        try (ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream())) {
            for (UUID id : ids) {
                try (FileResource resource = fileDocumentService.downloadFile(id)) {
                    if (resource != null) {
                        ZipEntry zipEntry = new ZipEntry(resource.getFileName());
                        zipOut.putNextEntry(zipEntry);
                        resource.getInputStream().transferTo(zipOut);
                        zipOut.closeEntry();
                    }
                } catch (Exception ex) {
                    log.error("Error zipping file with id {}: {}", id, ex.getMessage());
                }
            }
            zipOut.finish();
        }
    }

    @Operation(summary = "Delete a file")
    @ApiResponse(responseCode = "200", description = "File deleted successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
    @DeleteMapping("/delete-file/{id}")
    public ResponseEntity<ApiMessageDTO<Void>> deleteFile(@Parameter(description = "File ID") @PathVariable UUID id) {
        fileDocumentService.deleteFile(id);

        ApiMessageDTO<Void> apiMessageDTO = ApiMessageDTO.<Void>builder()
                .result(true)
                .message(messageService.getMessage("file.deleted"))
                .code(HttpStatus.OK.value())
                .build();

        return ResponseEntity.ok(apiMessageDTO);
    }

    @Operation(summary = "Delete multiple files")
    @ApiResponse(responseCode = "200", description = "Files deleted successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
    @DeleteMapping("/delete-file/batch")
    public ResponseEntity<ApiMessageDTO<Void>> deleteFiles(@RequestBody List<UUID> ids) {
        fileDocumentService.deleteFiles(ids);

        ApiMessageDTO<Void> apiMessageDTO = ApiMessageDTO.<Void>builder()
                .result(true)
                .message(messageService.getMessage("file.deleted"))
                .code(HttpStatus.OK.value())
                .build();

        return ResponseEntity.ok(apiMessageDTO);
    }
}
