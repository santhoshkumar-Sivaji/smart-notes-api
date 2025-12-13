package com.example.handwrittennotesapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LocalFileStorageService implements FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(LocalFileStorageService.class);

    @Override
    public String storeFile(MultipartFile file) {
        // In a real application, this would save the file to a local or cloud storage.
        // For now, we'll just log it and return a dummy path.
        logger.info("Storing file: {}", file.getOriginalFilename());
        return "uploads/" + file.getOriginalFilename();
    }
}
