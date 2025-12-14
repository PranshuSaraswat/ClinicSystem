package com.clinic.notificationservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.clinic.notificationservice.model.Notification;

@Service
public class NotificationService {

    private static final Logger log =
            LoggerFactory.getLogger(NotificationService.class);

    private final JavaMailSender mailSender;

    // 🔒 Static demo email
    private static final String DEMO_EMAIL = "saraswatpranshu@gmail.com";

    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(Notification notification) {

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(DEMO_EMAIL);   // STATIC EMAIL
        mail.setSubject("Clinic Appointment Notification");
        mail.setText(notification.getMessage());

        mailSender.send(mail);

        log.info("📧 Email sent to patient at {}", DEMO_EMAIL);
    }
}
