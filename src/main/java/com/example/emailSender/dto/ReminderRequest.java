package com.example.emailSender.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReminderRequest {
    private String patientId;
    private String medicationId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int intervalMinutes;
}