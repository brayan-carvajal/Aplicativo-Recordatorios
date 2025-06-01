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
public class Medication {
    private String id;
    private String name;
    private String dosage;
    private LocalDateTime createdAt;
    
    public Medication(String name, String dosage) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.dosage = dosage;
        this.createdAt = LocalDateTime.now();
    }
}