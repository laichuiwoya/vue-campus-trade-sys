package com.campus.trade.controller;

import com.campus.trade.common.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private static final Set<String> IMAGE_EXTENSIONS = Set.of(".jpg", ".jpeg", ".png", ".gif", ".webp");

    @PostMapping("/upload")
    public ApiResponse<Map<String, String>> upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ApiResponse.fail(400, "File is empty");
        }

        String originalName = file.getOriginalFilename() == null ? "" : file.getOriginalFilename();
        String extension = getExtension(originalName);
        if (!IMAGE_EXTENSIONS.contains(extension)) {
            return ApiResponse.fail(400, "Only image files are allowed");
        }

        String datePath = LocalDate.now().toString();
        String fileName = UUID.randomUUID() + extension;
        Path uploadDir = Path.of(System.getProperty("user.dir"), "uploads", datePath);
        Files.createDirectories(uploadDir);

        Path target = uploadDir.resolve(fileName);
        file.transferTo(target);

        String url = "http://localhost:8080/uploads/" + datePath + "/" + fileName;
        return ApiResponse.success(Map.of("url", url));
    }

    private String getExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index < 0) {
            return "";
        }
        return fileName.substring(index).toLowerCase(Locale.ROOT);
    }
}
