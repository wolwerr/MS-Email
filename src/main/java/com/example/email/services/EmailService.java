package com.example.email.services;

import com.example.email.enums.StatusEmail;
import com.example.email.models.EmailModel;
import com.example.email.repositories.EmailRepository;
import com.example.email.template.ContactEmailTemplate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final EmailRepository emailRepository;
    private final RestTemplate restTemplate;
    private final ContactEmailTemplate contactEmailTemplate = new ContactEmailTemplate();

    @Value("${resend.api-key}")
    private String resendApiKey;

    @Value("${resend.from:}")
    private String defaultFrom;

    @Transactional
    public void sendEmail(EmailModel emailModel) {
        emailModel.setSendDateEmail(LocalDateTime.now());

        if (emailModel.getEmailFrom() == null || emailModel.getEmailFrom().isBlank()) {
            if (defaultFrom != null && !defaultFrom.isBlank()) {
                emailModel.setEmailFrom(defaultFrom);
            }
        }

        try {
            String url = "https://api.resend.com/emails";

            String html = contactEmailTemplate.buildHtml(emailModel);

            Map<String, Object> body = new HashMap<>();
            body.put("from", emailModel.getEmailFrom());
            body.put("to", Collections.singletonList(emailModel.getEmailTo()));
            body.put("subject", emailModel.getSubject());
            body.put("html", html);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(resendApiKey);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            log.info("Enviando e-mail via Resend. From={} To={} Subject={}",
                    emailModel.getEmailFrom(), emailModel.getEmailTo(), emailModel.getSubject());

            ResponseEntity<String> response =
                    restTemplate.postForEntity(url, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                emailModel.setStatusEmail(StatusEmail.SENT);
                log.info("E-mail enviado com sucesso via Resend. Status={} Body={}",
                        response.getStatusCode(), response.getBody());
            } else {
                emailModel.setStatusEmail(StatusEmail.ERROR);
                log.error("Falha ao enviar e-mail via Resend. Status={} Body={}",
                        response.getStatusCode(), response.getBody());
            }

        } catch (Exception e) {
            emailModel.setStatusEmail(StatusEmail.ERROR);
            log.error("Exceção ao enviar e-mail via Resend", e);
        }

        emailRepository.save(emailModel);
    }

    public Page<EmailModel> findAll(Pageable pageable) {
        return emailRepository.findAll(pageable);
    }

    public Optional<EmailModel> findById(UUID emailId) {
        return emailRepository.findById(emailId);
    }
}
