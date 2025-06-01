package com.example.emailSender.controller;

import com.example.emailSender.dto.MedicationRequest;
import com.example.emailSender.dto.PatientRequest;
import com.example.emailSender.dto.ReminderRequest;
import com.example.emailSender.model.Medication;
import com.example.emailSender.model.Patient;
import com.example.emailSender.model.ReminderData;
import com.example.emailSender.services.MedicationService;
import com.example.emailSender.services.PatientService;
import com.example.emailSender.services.ReminderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MedicationReminderController {

    private final ReminderService reminderService;
    private final PatientService patientService;
    private final MedicationService medicationService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    // --- PACIENTES ---
    @PostMapping("/api/patients")
    @ResponseBody
    public ResponseEntity<String> createPatient(@RequestBody PatientRequest request) {
        log.info("Creando paciente: {} - {}", request.getName(), request.getEmail());
        
        try {
            Patient patient = patientService.createPatient(request.getName(), request.getEmail());
            return ResponseEntity.ok("Paciente creado exitosamente: " + patient.getName());
        } catch (Exception e) {
            log.error("Error en createPatient: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/api/patients")
    @ResponseBody
    public ResponseEntity<List<Patient>> getAllPatients() {
        try {
            List<Patient> patients = patientService.getAllPatients();
            return ResponseEntity.ok(patients);
        } catch (Exception e) {
            log.error("Error obteniendo pacientes: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/api/patients/{id}")
    @ResponseBody
    public ResponseEntity<String> updatePatient(@PathVariable String id, @RequestBody PatientRequest request) {
        log.info("Actualizando paciente: {}", id);
        
        try {
            boolean updated = patientService.updatePatient(id, request.getName(), request.getEmail());
            return updated ? 
                ResponseEntity.ok("Paciente actualizado exitosamente") :
                ResponseEntity.badRequest().body("Paciente no encontrado");
        } catch (Exception e) {
            log.error("Error en updatePatient: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/api/patients/{id}")
    @ResponseBody
    public ResponseEntity<String> deletePatient(@PathVariable String id) {
        log.info("Eliminando paciente: {}", id);
        
        try {
            boolean deleted = patientService.deletePatient(id);
            return deleted ? 
                ResponseEntity.ok("Paciente eliminado exitosamente") :
                ResponseEntity.badRequest().body("Paciente no encontrado");
        } catch (Exception e) {
            log.error("Error en deletePatient: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    // --- MEDICAMENTOS ---
    @PostMapping("/api/medications")
    @ResponseBody
    public ResponseEntity<String> createMedication(@RequestBody MedicationRequest request) {
        log.info("Creando medicamento: {} - {}", request.getName(), request.getDosage());
        
        try {
            Medication medication = medicationService.createMedication(request.getName(), request.getDosage());
            return ResponseEntity.ok("Medicamento creado exitosamente: " + medication.getName());
        } catch (Exception e) {
            log.error("Error en createMedication: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/api/medications")
    @ResponseBody
    public ResponseEntity<List<Medication>> getAllMedications() {
        try {
            List<Medication> medications = medicationService.getAllMedications();
            return ResponseEntity.ok(medications);
        } catch (Exception e) {
            log.error("Error obteniendo medicamentos: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/api/medications/{id}")
    @ResponseBody
    public ResponseEntity<String> updateMedication(@PathVariable String id, @RequestBody MedicationRequest request) {
        log.info("Actualizando medicamento: {}", id);
        
        try {
            boolean updated = medicationService.updateMedication(id, request.getName(), request.getDosage());
            return updated ? 
                ResponseEntity.ok("Medicamento actualizado exitosamente") :
                ResponseEntity.badRequest().body("Medicamento no encontrado");
        } catch (Exception e) {
            log.error("Error en updateMedication: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/api/medications/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteMedication(@PathVariable String id) {
        log.info("Eliminando medicamento: {}", id);
        
        try {
            boolean deleted = medicationService.deleteMedication(id);
            return deleted ? 
                ResponseEntity.ok("Medicamento eliminado exitosamente") :
                ResponseEntity.badRequest().body("Medicamento no encontrado");
        } catch (Exception e) {
            log.error("Error en deleteMedication: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    // --- RECORDATORIOS ---
    @PostMapping("/api/reminder/activate")
    @ResponseBody
    public ResponseEntity<String> activateReminder(@RequestBody ReminderRequest request) {
        log.info("Activando recordatorio para paciente: {} y medicamento: {}", 
                request.getPatientId(), request.getMedicationId());
        
        try {
            if (reminderService.hasActiveReminder(request.getPatientId(), request.getMedicationId())) {
                return ResponseEntity.badRequest().body("Ya existe un recordatorio activo para este paciente y medicamento");
            }

            boolean activated = reminderService.activateReminder(
                request.getPatientId(), 
                request.getMedicationId(),
                request.getStartDate(), 
                request.getEndDate(), 
                request.getIntervalMinutes()
            );

            return activated ? 
                ResponseEntity.ok("Recordatorio de medicamento activado exitosamente") :
                ResponseEntity.internalServerError().body("Error al activar recordatorio");
                
        } catch (Exception e) {
            log.error("Error en activateReminder: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/api/reminder/deactivate/{id}")
    @ResponseBody
    public ResponseEntity<String> deactivateReminder(@PathVariable String id) {
        log.info("Desactivando recordatorio: {}", id);
        
        try {
            boolean deactivated = reminderService.deactivateReminder(id);
            return deactivated ? 
                ResponseEntity.ok("Recordatorio desactivado exitosamente") :
                ResponseEntity.badRequest().body("Recordatorio no encontrado");
        } catch (Exception e) {
            log.error("Error en deactivateReminder: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/reminder/unsubscribe/{token}")
    public String unsubscribeReminder(@PathVariable String token) {
        log.info("Desuscripción con token: {}", token);
        
        try {
            boolean success = reminderService.unsubscribeByToken(token);
            return success ? 
                "redirect:/?message=unsubscribed" : 
                "redirect:/?error=invalid_token";
        } catch (Exception e) {
            log.error("Error en unsubscribeReminder: {}", e.getMessage(), e);
            return "redirect:/?error=system_error";
        }
    }

    @GetMapping("/api/reminder/history")
    @ResponseBody
    public ResponseEntity<List<ReminderData>> getReminderHistory() {
        try {
            List<ReminderData> reminders = reminderService.getAllActiveReminders();
            return ResponseEntity.ok(reminders);
        } catch (Exception e) {
            log.error("Error obteniendo historial: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/api/reminder/disable-all")
    @ResponseBody
    public ResponseEntity<String> disableAllReminders() {
        try {
            reminderService.disableAllReminders();
            return ResponseEntity.ok("Todos los recordatorios desactivados");
        } catch (Exception e) {
            log.error("Error desactivando todos: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/api/reminder/enable-all")
    @ResponseBody
    public ResponseEntity<String> enableAllReminders() {
        try {
            reminderService.enableAllReminders();
            return ResponseEntity.ok("Todos los recordatorios activados");
        } catch (Exception e) {
            log.error("Error activando todos: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}