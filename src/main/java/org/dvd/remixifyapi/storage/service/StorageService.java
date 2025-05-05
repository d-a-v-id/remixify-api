package org.dvd.remixifyapi.storage.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface StorageService {
    String uploadFile(MultipartFile file, FileType fileType) throws IOException;
    byte[] downloadFile(String fileKey) throws IOException;
    void deleteFile(String fileKey) throws IOException;

    enum FileType {
        AVATAR("uploads/avatars/"),
        RECIPE("uploads/recipes/");

        private final String prefix;

        FileType(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }
    }
} 