package com.example.emailSender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EmailSenderApplication {

	public static void main(String[] args) {
        SpringApplication.run(EmailSenderApplication.class, args);
        System.out.println("🚀 Sistema de Emails y Recordatorios iniciado");
        System.out.println("📧 Accede a: http://localhost:8080");
        System.out.println("🔔 Recordatorios programados activados");
    }
}
