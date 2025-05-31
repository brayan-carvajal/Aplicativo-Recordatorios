package com.example.demo.service;

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

    public void basicMail() {
        try {
            // destinatario
            String addressMail = "chochosick8@gmail.com";
            // Asunto
            String subject = "Este es un correo de prueba";
            // Cuerpo Correo
            String bodyMail = "Correo prueba";
            emailSender(addressMail, subject, bodyMail);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error en basicMail: " + e.getMessage(), e);
        }
    }

    public void advancedEmail(String addressMail) {
        try {
            // Asunto
            String subject = "Este es un correo de prueba";
            // Cuerpo Correo (código HTML existente)
            String bodyMail = "<!DOCTYPE html>..."; // Tu código HTML actual

            emailSender(addressMail, subject, bodyMail);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error en advancedEmail: " + e.getMessage(), e);
        }
    }

    // Nuevos métodos para los diferentes tipos de emails

    public boolean sendNewAccountEmail(String addressMail) {
        String subject = "Bienvenido - Nueva Cuenta Creada";
        String bodyMail = ""
                + "<!DOCTYPE html>\n"
                + "<html lang=\"es\">\n"
                + "<head>\n"
                + "    <meta charset=\"UTF-8\">\n"
                + "    <title>Bienvenido a Nuestra Plataforma</title>\n"
                + "    <style>\n"
                + "        body {\n"
                + "            font-family: Arial, sans-serif;\n"
                + "            background-color: #f4f4f4;\n"
                + "            margin: 0;\n"
                + "            padding: 0;\n"
                + "        }\n"
                + "        .correo-container {\n"
                + "            background-color: #ffffff;\n"
                + "            max-width: 600px;\n"
                + "            margin: 40px auto;\n"
                + "            padding: 30px;\n"
                + "            border-radius: 8px;\n"
                + "            box-shadow: 0 0 8px rgba(0, 0, 0, 0.1);\n"
                + "        }\n"
                + "        h2 {\n"
                + "            color: #333333;\n"
                + "        }\n"
                + "        p {\n"
                + "            font-size: 16px;\n"
                + "            color: #555555;\n"
                + "        }\n"
                + "        .btn {\n"
                + "            display: inline-block;\n"
                + "            margin-top: 20px;\n"
                + "            padding: 12px 20px;\n"
                + "            background-color: #007BFF;\n"
                + "            color: white;\n"
                + "            text-decoration: none;\n"
                + "            border-radius: 5px;\n"
                + "        }\n"
                + "        .footer {\n"
                + "            margin-top: 30px;\n"
                + "            font-size: 12px;\n"
                + "            color: #aaaaaa;\n"
                + "            text-align: center;\n"
                + "        }\n"
                + "    </style>\n"
                + "</head>\n"
                + "<body>\n"
                + "    <div class=\"correo-container\">\n"
                + "        <h2>¡Bienvenido a Nuestra Plataforma!</h2>\n"
                + "        <p>¡Gracias por registrarte! Tu cuenta ha sido creada exitosamente.</p>\n"
                + "        <p>Ahora puedes acceder a todos nuestros servicios utilizando tus credenciales de inicio de sesión.</p>\n"
                + "        <p>Haz clic en el botón a continuación para comenzar:</p>\n"
                + "        <a href=\"https://www.tuapp.com/login\" class=\"btn\">Iniciar Sesión</a>\n"
                + "        <p>Si tienes alguna pregunta o necesitas ayuda, no dudes en contactarnos.</p>\n"
                + "        <div class=\"footer\">\n"
                + "            © 2025 Tu Empresa. Todos los derechos reservados.\n"
                + "        </div>\n"
                + "    </div>\n"
                + "</body>\n"
                + "</html>";

        try {
            return emailSender(addressMail, subject, bodyMail);
        } catch (MessagingException e) {
            logger.log(Level.SEVERE, "Error en sendNewAccountEmail: " + e.getMessage(), e);
            return false;
        }
    }
    

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