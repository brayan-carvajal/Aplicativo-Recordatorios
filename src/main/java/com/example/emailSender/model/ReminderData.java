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
public class ReminderData {
    private String id;
    private String patientId;
    private String medicationId;
    private String patientName;
    private String patientEmail;
    private String medicationName;
    private String medicationDosage;
    private String unsubscribeToken;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime nextSendTime;
    private int intervalMinutes;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime lastSent;

    public ReminderData(String patientId, String medicationId, String patientName, 
                       String patientEmail, String medicationName, String medicationDosage,
                       LocalDateTime startDate, LocalDateTime endDate, int intervalMinutes) {
        this.id = UUID.randomUUID().toString();
        this.patientId = patientId;
        this.medicationId = medicationId;
        this.patientName = patientName;
        this.patientEmail = patientEmail;
        this.medicationName = medicationName;
        this.medicationDosage = medicationDosage;
        this.unsubscribeToken = UUID.randomUUID().toString();
        this.startDate = startDate;
        this.endDate = endDate;
        this.intervalMinutes = intervalMinutes;
        this.nextSendTime = startDate;
        this.active = true;
        this.createdAt = LocalDateTime.now();
        this.lastSent = null;
    }

    public boolean shouldSend(LocalDateTime now) {
        return active &&
                !now.isBefore(startDate) &&
                !now.isAfter(endDate) &&
                !now.isBefore(nextSendTime);
    }

    public void updateNextSendTime() {
        this.nextSendTime = LocalDateTime.now().plusMinutes(intervalMinutes);
        this.lastSent = LocalDateTime.now();
    }

    public boolean isExpired(LocalDateTime now) {
        return now.isAfter(endDate);
    }
}