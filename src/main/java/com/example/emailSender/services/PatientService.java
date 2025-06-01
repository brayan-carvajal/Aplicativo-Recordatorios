package com.example.emailSender.services;

import com.example.emailSender.model.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class PatientService {

    private final ConcurrentHashMap<String, Patient> patients = new ConcurrentHashMap<>();

    public Patient createPatient(String name, String email) {
        try {
            // Verificar si ya existe un paciente con ese email
            boolean emailExists = patients.values().stream()
                    .anyMatch(patient -> email.equals(patient.getEmail()));
            
            if (emailExists) {
                throw new RuntimeException("Ya existe un paciente con ese correo electrónico");
            }

            Patient patient = new Patient(name, email);
            patients.put(patient.getId(), patient);
            log.info("Paciente creado: {} - {}", patient.getName(), patient.getEmail());
            return patient;
        } catch (Exception e) {
            log.error("Error al crear paciente: {}", e.getMessage(), e);
            throw e;
        }
    }

    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients.values());
    }

    public Optional<Patient> getPatientById(String id) {
        return Optional.ofNullable(patients.get(id));
    }

    public boolean updatePatient(String id, String name, String email) {
        try {
            Patient patient = patients.get(id);
            if (patient == null) {
                return false;
            }

            // Verificar si el nuevo email ya existe en otro paciente
            boolean emailExists = patients.values().stream()
                    .anyMatch(p -> !p.getId().equals(id) && email.equals(p.getEmail()));
            
            if (emailExists) {
                throw new RuntimeException("Ya existe otro paciente con ese correo electrónico");
            }

            patient.setName(name);
            patient.setEmail(email);
            log.info("Paciente actualizado: {} - {}", patient.getName(), patient.getEmail());
            return true;
        } catch (Exception e) {
            log.error("Error al actualizar paciente: {}", e.getMessage(), e);
            throw e;
        }
    }

    public boolean deletePatient(String id) {
        try {
            Patient removed = patients.remove(id);
            if (removed != null) {
                log.info("Paciente eliminado: {}", removed.getName());
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("Error al eliminar paciente: {}", e.getMessage(), e);
            return false;
        }
    }
}