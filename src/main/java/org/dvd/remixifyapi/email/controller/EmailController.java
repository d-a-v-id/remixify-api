package org.dvd.remixifyapi.email.controller;

import org.dvd.remixifyapi.email.dto.ContactFormRequest;
import org.dvd.remixifyapi.email.dto.EmailRequestDto;
import org.dvd.remixifyapi.email.dto.EmailResponseDto;
import org.dvd.remixifyapi.email.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
@Slf4j
public class EmailController {

    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<EmailResponseDto> sendEmail(
            @Validated @RequestBody EmailRequestDto emailRequest) {
        log.debug("Sending email to: {}", emailRequest.getTo());

        String messageId = emailService.sendEmail(
                emailRequest.getTo(),
                emailRequest.getSubject(),
                emailRequest.getHtml());

        EmailResponseDto response = EmailResponseDto.builder()
                .messageId(messageId)
                .status("sent")
                .recipient(emailRequest.getTo())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/contact")
    public ResponseEntity<EmailResponseDto> sendContactEmail(
            @Validated @RequestBody ContactFormRequest contactForm) {
        log.debug("Sending contact email from {}", contactForm.getEmail());

        String messageId = emailService.sendContactEmail(contactForm);

        EmailResponseDto response = EmailResponseDto.builder()
                .messageId(messageId)
                .status("sent")
                .recipient(contactForm.getEmail())
                .build();

        return ResponseEntity.ok(response);
    }
}