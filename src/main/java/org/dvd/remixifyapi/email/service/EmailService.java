package org.dvd.remixifyapi.email.service;

import java.util.Map;

import org.dvd.remixifyapi.shared.config.AppProperties;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final AppProperties appProperties;
    private final TemplateEngine templateEngine;

    public String sendTemplatedEmail(String to, String subject, String template, Map<String, Object> variables) {
        Context context = new Context();
        variables.forEach(context::setVariable);

        String html = templateEngine.process(template, context);
        return sendEmail(to, subject, html);
    }

    public String sendEmail(String to, String subject, String html) {
        Resend resend = new Resend(appProperties.getEmail().getApiKey());

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(appProperties.getEmail().getFrom())
                .to(to)
                .subject(subject)
                .html(html)
                .build();

        // For development purposes, disable email sending
        if (!appProperties.getEmail().isEnabled()) {
            log.info("Email sending is disabled. Email would be sent to: {}", to);
            return "dev-mode";
        }

        try {
            CreateEmailResponse data = resend.emails().send(params);
            log.info("Email sent successfully with ID: {}", data.getId());
            return data.getId();
        } catch (ResendException e) {
            log.error("Failed to send email: {}", e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
