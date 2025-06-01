package com.example.emailSender.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class emailService {

    private static final Logger logger = Logger.getLogger(emailService.class.getName());

    @Autowired
    private JavaMailSender javaMailSender;

    public boolean emailSender(String addressMail, String subject, String bodyMail) throws MessagingException {
        try {
            logger.info("Intentando enviar correo a: " + addressMail);

            // creación del correo
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(addressMail);
            helper.setSubject(subject);
            helper.setText(bodyMail, true);

            logger.info("Enviando mensaje...");
            javaMailSender.send(message);
            logger.info("Correo enviado exitosamente");

            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al enviar correo: " + e.getMessage(), e);
            throw new MessagingException("Error al enviar el correo: " + e.getMessage());
        }
    }
}