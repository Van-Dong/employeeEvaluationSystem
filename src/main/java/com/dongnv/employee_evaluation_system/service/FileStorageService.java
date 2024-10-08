package com.dongnv.employee_evaluation_system.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@Slf4j
public class FileStorageService {
    private final String uploadDir = "C:/Users/dongnv/Documents/Assignment/Day-2/employee_evaluation_system/upload_images/";
    private final String uploadImageDir = "C:/Users/dongnv/Documents/Assignment/Day-2/employee_evaluation_system/upload_images/images/";
//private final String uploadImageDir = "C:/Users/HELLO/Documents/Assignment/Day-2/employeeEvaluationSystem/upload_images/images/";

    public String storeFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        Path uploadPath = Paths.get(uploadImageDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectory(uploadPath);
        }


        String fileName = Instant.now().getEpochSecond() + file.getOriginalFilename();
        String filePath = uploadPath.resolve(fileName).toString();

        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

        return "images/" + fileName;
    }

    public void deleteFile(String fileName) {
        if (fileName != null && !fileName.isEmpty()) {
            if (fileName.indexOf("/") == fileName.lastIndexOf("/")) {
                Path filePath = Paths.get(uploadDir + fileName);
                try {
                    Files.delete(filePath);
                    log.info("File {} was deleted!", fileName);
                } catch (IOException e) {
                    log.info("File could not be deleted: {}", e.getMessage());
                }
            }
        }
    }
}
