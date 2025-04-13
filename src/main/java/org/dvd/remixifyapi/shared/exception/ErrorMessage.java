package org.dvd.remixifyapi.shared.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ErrorMessage {
    private int status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "UTC")
    private Instant timestamp;
    private String message;
    private String path;
    @Builder.Default
    private List<ValidationError> errors = new ArrayList<>();

    @Data
    @Builder
    public static class ValidationError {
        private String field;
        private String message;
    }

    public static ErrorMessage of(int status, String message, String path) {
        return ErrorMessage.builder()
                .status(status)
                .message(message)
                .path(path)
                .timestamp(Instant.now())
                .build();
    }
}