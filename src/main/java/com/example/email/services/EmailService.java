package com.example.email.services;

import com.example.email.enums.StatusEmail;
import com.example.email.models.EmailModel;
import com.example.email.repositories.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {


    private final EmailRepository emailRepository;


    private final JavaMailSender emailSender;

    public void sendEmail(EmailModel emailModel) {
        emailModel.setSendDateEmail(LocalDateTime.now());
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailModel.getEmailFrom());
            message.setTo(emailModel.getEmailTo());
            message.setBcc("ricardo@dtmm.com.br");
            message.setSubject(emailModel.getSubject());
            String emailBody = emailModel.getText() + "\nPhone: " + emailModel.getPhone();
            message.setText(emailBody);
            emailSender.send(message);
            emailModel.setStatusEmail(StatusEmail.SENT);
            System.out.println("E-mail enviado");
        } catch (MailException e){
            emailModel.setStatusEmail(StatusEmail.ERROR);
            System.out.println("Não foi possível enviar o e-mail");
        } finally {
            emailRepository.save(emailModel);
            return;
        }
    }

    public Page<EmailModel> findAll(Pageable pageable) {
        return  emailRepository.findAll(pageable);
    }

    public Optional<EmailModel> findById(UUID emailId) { return emailRepository.findById(emailId);
    }
}
