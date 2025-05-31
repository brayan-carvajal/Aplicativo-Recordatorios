package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.service.emailService;

import java.util.logging.Logger;

@Controller
public class emailController {

    private static final Logger logger = Logger.getLogger(emailController.class.getName());

    @Autowired
    private emailService emailService;

    // Página principal que muestra el formulario
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // API endpoints para los diferentes tipos de emails
    @GetMapping("/api/email/newAccount/{email}")
    @ResponseBody
    public ResponseEntity<String> sendNewAccountEmail(@PathVariable String email) {
        logger.info("Solicitud para enviar correo de nueva cuenta a: " + email);
        try {
            boolean sent = emailService.sendNewAccountEmail(email);
            if (sent) {
                return ResponseEntity.ok("Correo de nueva cuenta enviado exitosamente");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al enviar el correo de nueva cuenta");
            }
        } catch (Exception e) {
            logger.severe("Error en sendNewAccountEmail: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al enviar el correo: " + e.getMessage());
        }
    }

    // Endpoints antiguos para mantener compatibilidad
    @GetMapping("/basicEmail")
    @ResponseBody
    public String sendBasicEmail() {
        emailService.basicMail();
        return "mail sent successfully";
    }

    @GetMapping("/advancedEmail/{email}")
    @ResponseBody
    public String advancedEmail(@PathVariable String email) {
        emailService.advancedEmail(email);
        return "mail sent successfully";
    }
}