package org.dvd.remixifyapi.storage.util;

public interface FileStorageConstants {
    String BASE_UPLOAD_DIR = "uploads";
    String RECIPES_DIR = BASE_UPLOAD_DIR + "/recipes";
    String AVATARS_DIR = BASE_UPLOAD_DIR + "/avatars";
    String DEFAULT_AVATAR = "uploads/avatars/default_avatar.png";
    
    // URL paths for accessing the files
    String UPLOAD_URL_PATH = "/uploads";
    String RECIPES_URL_PATH = UPLOAD_URL_PATH + "/recipes";
    String AVATARS_URL_PATH = UPLOAD_URL_PATH + "/avatars";
}