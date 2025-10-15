package com.community_centers.service.controller;

import com.community_centers.service.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/uploads")
@RequiredArgsConstructor
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/bmp",
            "image/webp", "image/svg+xml", "image/tiff", "image/avif"
    );

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
            ".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp", ".svg", ".tiff", ".tif", ".avif"
    );

    private final FileStorageService fileStorageService;

    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        logger.info("File upload request received - Name: {}, Size: {}, Type: {}",
                file.getOriginalFilename(), file.getSize(), file.getContentType());

        try {
            if (file.isEmpty()) {
                logger.warn("Empty file uploaded");
                return ResponseEntity.badRequest().body(
                        Map.of("error", "File is empty")
                );
            }

            String contentType = file.getContentType();
            if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
                logger.warn("Invalid file type: {}", contentType);
                return ResponseEntity.badRequest().body(
                        Map.of("error", "Unsupported file type. Allowed types: " +
                                "JPEG, JPG, PNG, GIF, BMP, WEBP, SVG, TIFF, AVIF")
                );
            }

            String originalFileName = file.getOriginalFilename();
            if (originalFileName != null) {
                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();
                if (!ALLOWED_EXTENSIONS.contains(fileExtension)) {
                    logger.warn("Invalid file extension: {}", fileExtension);
                    return ResponseEntity.badRequest().body(
                            Map.of("error", "Unsupported file extension. Allowed: " + ALLOWED_EXTENSIONS)
                    );
                }
            }

            if (file.getSize() > 10 * 1024 * 1024) {
                logger.warn("File too large: {} bytes", file.getSize());
                return ResponseEntity.badRequest().body(
                        Map.of("error", "File size exceeds 10MB limit")
                );
            }

            String fileName = fileStorageService.storeFile(file);
            logger.info("File stored successfully: {}", fileName);

            Map<String, String> response = new HashMap<>();
            response.put("fileName", fileName);
            response.put("message", "File uploaded successfully");
            response.put("originalName", file.getOriginalFilename());
            response.put("size", String.valueOf(file.getSize()));
            response.put("contentType", file.getContentType());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("File upload failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "File upload failed: " + e.getMessage())
            );
        }
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<byte[]> getFile(@PathVariable String fileName) {
        try {
            byte[] fileContent = fileStorageService.loadFile(fileName);

            String contentType = determineContentType(fileName);

            return ResponseEntity.ok()
                    .header("Content-Type", contentType)
                    .body(fileContent);
        } catch (Exception e) {
            logger.error("File retrieval failed: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    private String determineContentType(String fileName) {
        if (fileName.toLowerCase().endsWith(".png")) {
            return "image/png";
        } else if (fileName.toLowerCase().endsWith(".gif")) {
            return "image/gif";
        } else if (fileName.toLowerCase().endsWith(".bmp")) {
            return "image/bmp";
        } else if (fileName.toLowerCase().endsWith(".webp")) {
            return "image/webp";
        } else if (fileName.toLowerCase().endsWith(".svg")) {
            return "image/svg+xml";
        } else if (fileName.toLowerCase().endsWith(".tiff") || fileName.toLowerCase().endsWith(".tif")) {
            return "image/tiff";
        } else if (fileName.toLowerCase().endsWith(".avif")) {
            return "image/avif";
        } else {
            return "image/jpeg";
        }
    }
}