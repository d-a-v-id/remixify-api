package org.dvd.remixifyapi.shared.config;

import org.dvd.remixifyapi.storage.util.FileStorageConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        registry.addResourceHandler(FileStorageConstants.UPLOAD_URL_PATH + "/**")
                .addResourceLocations("file:" + FileStorageConstants.BASE_UPLOAD_DIR + "/")
                .setCachePeriod(3600)
                .resourceChain(true);
    }
}