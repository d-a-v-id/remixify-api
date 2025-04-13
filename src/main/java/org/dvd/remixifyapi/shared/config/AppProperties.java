package org.dvd.remixifyapi.shared.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "app")
@EnableConfigurationProperties
public class AppProperties {
    private Security security;
    private Storage storage;
    private Email email;

    @Data
    public static class Security {
        private JwtConfiguration jwt = new JwtConfiguration();

        @Data
        public static class JwtConfiguration {
            private String secretKey;
            private Long expirationTime;
            private RefreshToken refreshToken = new RefreshToken();

            @Data
            public static class RefreshToken {
                private Long expirationTime;
            }
        }
    }

    @Data
    public static class Storage {
        private String uploadDir;
        private Long maxFileSize;
    }

    @Data
    public static class Email {
        private String apiKey;
        private String from;
        private boolean enabled;
    }
}