package com.example.emailSender.services;

import com.example.emailSender.model.Medication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class MedicationService {

    private final ConcurrentHashMap<String, Medication> medications = new ConcurrentHashMap<>();

    public Medication createMedication(String name, String dosage) {
        try {
            Medication medication = new Medication(name, dosage);
            medications.put(medication.getId(), medication);
            log.info("Medicamento creado: {} - {}", medication.getName(), medication.getDosage());
            return medication;
        } catch (Exception e) {
            log.error("Error al crear medicamento: {}", e.getMessage(), e);
            throw e;
        }
    }

    public List<Medication> getAllMedications() {
        return new ArrayList<>(medications.values());
    }

    public Optional<Medication> getMedicationById(String id) {
        return Optional.ofNullable(medications.get(id));
    }

    public boolean updateMedication(String id, String name, String dosage) {
        try {
            Medication medication = medications.get(id);
            if (medication == null) {
                return false;
            }

            medication.setName(name);
            medication.setDosage(dosage);
            log.info("Medicamento actualizado: {} - {}", medication.getName(), medication.getDosage());
            return true;
        } catch (Exception e) {
            log.error("Error al actualizar medicamento: {}", e.getMessage(), e);
            throw e;
        }
    }

    public boolean deleteMedication(String id) {
        try {
            Medication removed = medications.remove(id);
            if (removed != null) {
                log.info("Medicamento eliminado: {}", removed.getName());
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("Error al eliminar medicamento: {}", e.getMessage(), e);
            return false;
        }
    }
}