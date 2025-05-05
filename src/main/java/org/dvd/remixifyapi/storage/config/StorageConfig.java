package org.dvd.remixifyapi.storage.config;

import org.dvd.remixifyapi.storage.service.LocalStorageService;
import org.dvd.remixifyapi.storage.service.AwsService;
import org.dvd.remixifyapi.storage.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class StorageConfig {

    @Value("${app.storage.type:local}")
    private String storageType;

    @Bean
    @Primary
    public StorageService storageService(LocalStorageService localStorageService, AwsService s3Service) {
        return switch (storageType.toLowerCase()) {
            case "s3" -> s3Service;
            case "local" -> localStorageService;
            default -> throw new IllegalArgumentException("Invalid storage type: " + storageType);
        };
    }
} 