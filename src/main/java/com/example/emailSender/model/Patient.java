package com.example.emailSender.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {
    private String id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    
    public Patient(String name, String email) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.createdAt = LocalDateTime.now();
    }
}