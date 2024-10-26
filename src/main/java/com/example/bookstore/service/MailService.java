package com.example.bookstore.service;

import com.example.bookstore.entity.Users;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender sender;
    private final TemplateEngine templateEngine;

    public void sendMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        sender.send(message);
    }

    public void sendMail2(Users user, String subject, String link) {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(user.getEmail());
            helper.setSubject(subject);

            final Context context = new Context();
            context.setVariable("user", user);
            context.setVariable("link", link);

            // Create the HTML body using Thymeleaf
            final String htmlContent = templateEngine.process("src/main/resources/templates/mail/template-register.html", context);
            helper.setText(htmlContent, true);

            // Send Message!
            sender.send(message);
            System.out.println("Verification email sent to " + user.getEmail());
        } catch (MessagingException e) {
            System.err.println("Failed to send email to " + user.getEmail());
            throw new EmailSendingException("Could not send email to " + user.getEmail(), e);
        }
    }

    public static class EmailSendingException extends RuntimeException {
        public EmailSendingException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
