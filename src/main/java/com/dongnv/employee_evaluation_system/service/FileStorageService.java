package com.dongnv.employee_evaluation_system.service;

import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dongnv.employee_evaluation_system.exception.AppException;
import com.dongnv.employee_evaluation_system.exception.ErrorCode;

@Service
//@Slf4j
public class FileStorageService {
    private final String uploadDir;
    private final String uploadImageDir;

    public FileStorageService(@Value("${file.upload-dir}") String uploadDir,
                              @Value("${file.upload-image-dir}") String uploadImageDir) {
        this.uploadDir = uploadDir;
        this.uploadImageDir = uploadImageDir;
    }

    public String storeFile(MultipartFile file) {
        // File not exist --> return null
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            Path uploadPath = Paths.get(uploadImageDir);

            // Upload directory not exist --> create new
            if (!Files.exists(uploadPath)) {
                Files.createDirectory(uploadPath);
            }

            String fileName = Instant.now().getEpochSecond() + file.getOriginalFilename();
            String filePath = uploadPath.resolve(fileName).toString();

            Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
            return "images/" + fileName;
        } catch (IOException exception) {
            throw new AppException(ErrorCode.ERROR_WHEN_STORE_IMAGE);
        }
    }

    public void deleteFile(String fileName) {
        if (fileName != null && !fileName.isEmpty() && fileName.indexOf("/") == fileName.lastIndexOf("/")) {
            Path filePath = Paths.get(uploadDir + fileName);
            try {
                Files.deleteIfExists(filePath);
                System.out.println("Deleted file: " + filePath);
//                log.info("Deleted file: {}", filePath);
            } catch (IOException e) {
//                log.info("Error while deleting file");
                System.out.print("Error while deleting file");
            }
        }
    }
}
