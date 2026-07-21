package com.jcoder.emsbp.service;

import com.jcoder.emsbp.model.Email;
import com.jcoder.emsbp.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailService {
    @Autowired
    private ObjectProvider<JavaMailSender> mailSenderProvider;

    @Autowired
    private EmailRepository emailRepository;

    public void sendEmail(String to, String subject,String body){
        Email e = new Email();

        e.setRecipient(to);
        e.setSubject(subject);
        e.setMessage(body);
        e.setSentAt(LocalDateTime.now());

        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            JavaMailSender mailSender = mailSenderProvider.getIfAvailable();
            if (mailSender == null) {
                throw new IllegalStateException("No JavaMailSender bean configured");
            }

            mailSender.send(message);
            e.setStatus("SUCCESS");
        } catch (Exception ex) {
            e.setStatus("FAILURE");
            System.out.println(ex.getMessage());

        }
        emailRepository.save(e);
    }

    public void sendResetLink(String toEmail,String resetLink){
        String subject = "Password Reset Request Link";

        String body = "Hello\n\n Click the Below Link to Reset your password:\n" + resetLink + "\n\nIf you did not request a password reset, please ignore this email.\n\nThank you.";
        sendEmail(toEmail, subject, body);
    }

}
