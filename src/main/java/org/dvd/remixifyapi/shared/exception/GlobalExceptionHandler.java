package org.dvd.remixifyapi.shared.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Value("${spring.profiles.active:prod}")
    private String activeProfile;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleNotFound(
            EntityNotFoundException exception,
            WebRequest request) {
        ErrorMessage errorResponse = ErrorMessage.of(
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                getPath(request)
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationException(
            MethodArgumentNotValidException exception,
            WebRequest request) {
        List<ErrorMessage.ValidationError> validationErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> ErrorMessage.ValidationError.builder()
                        .field(error.getField())
                        .message(error.getDefaultMessage())
                        .build())
                .toList();

        ErrorMessage errorResponse = ErrorMessage.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed")
                .path(getPath(request))
                .timestamp(Instant.now())
                .errors(validationErrors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleUsernameNotFound(
            UsernameNotFoundException ex,
            WebRequest request) {
        ErrorMessage errorResponse = ErrorMessage.of(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                getPath(request)
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMessage> handleDataIntegrityViolation(
            DataIntegrityViolationException exception,
            WebRequest request) {
        ErrorMessage errorResponse = ErrorMessage.of(
                HttpStatus.CONFLICT.value(),
                "An account with this username or email address already exists",
                getPath(request)
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGlobalException(
            Exception exception,
            WebRequest request) {
        log.error("Unexpected error occurred", exception.getMessage());

        String message = isDev() 
            ? String.format("%s: %s", exception.getMessage(), 
                          exception.getCause() != null ? exception.getCause() : "No cause")
            : "An unexpected error occurred";

        ErrorMessage errorResponse = ErrorMessage.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                message,
                getPath(request)
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    private boolean isDev() {
        return "dev".equalsIgnoreCase(activeProfile);
    }

    private String getPath(WebRequest request) {
        return ((ServletWebRequest) request).getRequest().getRequestURI();
    }
}