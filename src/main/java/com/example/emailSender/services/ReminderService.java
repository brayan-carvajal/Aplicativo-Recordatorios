package com.example.emailSender.services;

import com.example.emailSender.model.Medication;
import com.example.emailSender.model.Patient;
import com.example.emailSender.model.ReminderData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReminderService {

    private final emailService emailService;
    private final PatientService patientService;
    private final MedicationService medicationService;
    private final ConcurrentHashMap<String, ReminderData> activeReminders = new ConcurrentHashMap<>();
    private volatile boolean globalReminderEnabled = true;

    public boolean activateReminder(String patientId, String medicationId, 
                                  LocalDateTime startDate, LocalDateTime endDate, int intervalMinutes) {
        try {
            // Verificar si ya existe un recordatorio activo para este paciente y medicamento
            if (hasActiveReminder(patientId, medicationId)) {
                return false;
            }

            // Obtener datos del paciente
            Optional<Patient> patientOpt = patientService.getPatientById(patientId);
            Optional<Medication> medicationOpt = medicationService.getMedicationById(medicationId);
            
            if (patientOpt.isEmpty() || medicationOpt.isEmpty()) {
                log.error("Paciente o medicamento no encontrado");
                return false;
            }

            Patient patient = patientOpt.get();
            Medication medication = medicationOpt.get();

            ReminderData reminder = new ReminderData(
                patientId, medicationId, patient.getName(), patient.getEmail(),
                medication.getName(), medication.getDosage(),
                startDate, endDate, intervalMinutes
            );
            
            activeReminders.put(reminder.getId(), reminder);
            
            log.info("Recordatorio de medicamento activado para: {} - Medicamento: {} desde {} hasta {} cada {} minutos", 
                    patient.getName(), medication.getName(), startDate, endDate, intervalMinutes);
            return true;
        } catch (Exception e) {
            log.error("Error al activar recordatorio: {}", e.getMessage(), e);
            return false;
        }
    }

    public boolean deactivateReminder(String reminderId) {
        try {
            ReminderData removed = activeReminders.remove(reminderId);
            if (removed != null) {
                log.info("Recordatorio desactivado para: {} - {}", removed.getPatientName(), removed.getMedicationName());
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("Error al desactivar recordatorio: {}", e.getMessage(), e);
            return false;
        }
    }

    public boolean unsubscribeByToken(String token) {
        try {
            return activeReminders.values().stream()
                    .filter(reminder -> token.equals(reminder.getUnsubscribeToken()))
                    .findFirst()
                    .map(reminder -> {
                        activeReminders.remove(reminder.getId());
                        log.info("Recordatorio desactivado por token para: {} - {}", 
                                reminder.getPatientName(), reminder.getMedicationName());
                        return true;
                    })
                    .orElse(false);
        } catch (Exception e) {
            log.error("Error al desuscribir por token: {}", e.getMessage(), e);
            return false;
        }
    }

    public void disableAllReminders() {
        globalReminderEnabled = false;
        log.info("Todos los recordatorios desactivados globalmente");
    }

    public void enableAllReminders() {
        globalReminderEnabled = true;
        log.info("Todos los recordatorios activados globalmente");
    }

    public List<ReminderData> getAllActiveReminders() {
        return new ArrayList<>(activeReminders.values());
    }

    public boolean hasActiveReminder(String patientId, String medicationId) {
        return activeReminders.values().stream()
                .anyMatch(reminder -> patientId.equals(reminder.getPatientId()) 
                        && medicationId.equals(reminder.getMedicationId()) 
                        && reminder.isActive());
    }

    public int getActiveRemindersCount() {
        return (int) activeReminders.values().stream()
                .filter(ReminderData::isActive)
                .count();
    }

    public boolean isGlobalReminderEnabled() {
        return globalReminderEnabled;
    }

    @Scheduled(fixedRate = 60000)
    public void checkAndSendReminders() {
        if (!globalReminderEnabled) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        List<String> expiredIds = new ArrayList<>();

        activeReminders.values().forEach(reminder -> {
            try {
                if (reminder.isExpired(now)) {
                    expiredIds.add(reminder.getId());
                    log.info("Recordatorio expirado para: {} - {}", 
                            reminder.getPatientName(), reminder.getMedicationName());
                } else if (reminder.shouldSend(now)) {
                    boolean sent = sendReminderEmail(reminder);
                    if (sent) {
                        reminder.updateNextSendTime();
                        log.info("Recordatorio enviado a: {} para medicamento: {}", 
                                reminder.getPatientName(), reminder.getMedicationName());
                    } else {
                        log.warn("Fallo al enviar recordatorio a: {} para medicamento: {}", 
                                reminder.getPatientName(), reminder.getMedicationName());
                    }
                }
            } catch (Exception e) {
                log.error("Error procesando recordatorio para {} - {}: {}", 
                        reminder.getPatientName(), reminder.getMedicationName(), e.getMessage(), e);
            }
        });

        // Remover recordatorios expirados
        expiredIds.forEach(activeReminders::remove);
    }

    private boolean sendReminderEmail(ReminderData reminder) {
        try {
            String subject = "💊 Recordatorio de Medicamento";
            String body = buildEmailBody(reminder);
            return emailService.emailSender(reminder.getPatientEmail(), subject, body);
        } catch (Exception e) {
            log.error("Error enviando recordatorio: {}", e.getMessage(), e);
            return false;
        }
    }

    private String buildEmailBody(ReminderData reminder) {
        String baseUrl = "http://localhost:8080";
        
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head><meta charset="UTF-8"><title>Recordatorio de Medicamento</title></head>
            <body style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px;">
                <h2>💊 Recordatorio de Medicamento</h2>
                <p>Hola <strong>%s</strong>,</p>
                <p>Es hora de tomar tu medicamento:</p>
                <div style="background: #e8f4fd; padding: 20px; border-radius: 10px; margin: 20px 0; border-left: 5px solid #007bff;">
                    <h3 style="margin-top: 0; color: #007bff;">💊 %s</h3>
                    <p><strong>📋 Dosis:</strong> %s</p>
                    <p><strong>⏰ Frecuencia:</strong> Cada %d minutos</p>
                    <p><strong>📅 Válido hasta:</strong> %s</p>
                </div>
                <div style="background: #fff3cd; padding: 15px; border-radius: 5px; border-left: 5px solid #ffc107; margin: 20px 0;">
                    <p style="margin: 0;"><strong>⚠️ Importante:</strong> Toma tu medicamento según las indicaciones médicas.</p>
                </div>
                <p style="text-align: center; margin: 30px 0;">
                    <a href="%s/reminder/unsubscribe/%s" 
                       style="background: #dc3545; color: white; padding: 12px 25px; text-decoration: none; border-radius: 5px; display: inline-block;">
                        🛑 Detener Recordatorios
                    </a>
                </p>
                <div style="border-top: 1px solid #eee; padding-top: 15px; margin-top: 30px; font-size: 12px; color: #666;">
                    <p>Este es un recordatorio automático. No respondas a este correo.</p>
                </div>
            </body>
            </html>
            """, 
            reminder.getPatientName(),
            reminder.getMedicationName(), 
            reminder.getMedicationDosage(),
            reminder.getIntervalMinutes(), 
            reminder.getEndDate(),
            baseUrl, 
            reminder.getUnsubscribeToken()
        );
    }
}