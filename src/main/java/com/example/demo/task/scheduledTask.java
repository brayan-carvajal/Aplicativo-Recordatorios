package com.example.demo.task;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.service.emailService;

@Component
public class scheduledTask {

    @Autowired
    private emailService email;

    @Scheduled(fixedRate = 1000)
    public void taskSegundo() {
        System.out.println("Tarea segundo " + LocalDateTime.now());
    }

    @Scheduled(fixedRate = 5000)
    public void task5Segundo() {
        System.out.println("Tarea 5 segundo " + LocalDateTime.now());
    }

    @Scheduled(cron = "0 35 9 * * * ")
    public void taskMinute() {
        System.out.println("Correo enviado");
    }
   
}