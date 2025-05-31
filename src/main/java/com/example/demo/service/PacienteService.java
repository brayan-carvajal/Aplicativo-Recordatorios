package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.example.demo.dto.PacienteDTO;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.model.Paciente;
import com.example.demo.repository.Ipaciente;

@Service
public class PacienteService {

    @Autowired
    private Ipaciente ipaciente;

    // Crear
    public ResponseDTO createPaciente(PacienteDTO pacienteDTO) {
        Paciente paciente = convertToEntity(pacienteDTO);
        paciente.setStatus(Paciente.Status.ACTIVE);
        paciente.setRegistrationDate(LocalDate.now());
        ipaciente.save(paciente);
        return new ResponseDTO(HttpStatus.CREATED.toString(), "Paciente guardado correctamente");
    }

    // Obtener
    public List<PacienteDTO> getAllPacientes() {
        return ipaciente.findByStatus(Paciente.Status.ACTIVE)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Obtener por id
    public Optional<PacienteDTO> getPacienteById(int id) {
        return ipaciente.findByIdAndStatus(id, Paciente.Status.ACTIVE)
                .map(this::convertToDTO);
    }

    // Actualizar
    public ResponseDTO updatePaciente(int id, PacienteDTO updatePacienteDTO) {
        Paciente paciente = ipaciente.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        if (paciente.getStatus() == Paciente.Status.SUSPENDED) {
            return new ResponseDTO(HttpStatus.FORBIDDEN.toString(), "No se puede actualizar el paciente");
        }
        if (updatePacienteDTO.getNombre() != null)
            paciente.setNombre(updatePacienteDTO.getNombre());
        if (updatePacienteDTO.getEmail() != null)
            paciente.setEmail(updatePacienteDTO.getEmail());

        ipaciente.save(paciente);
        return new ResponseDTO(HttpStatus.OK.toString(), "Paciente actualizado con exito");
    }

    // Eliminar
    public ResponseDTO deletePaciente(int id) {
        Optional<Paciente> pacienteOpt = ipaciente.findById(id);

        if (!pacienteOpt.isEmpty()) {
            return new ResponseDTO(HttpStatus.NOT_FOUND.toString(), "Paciente no encontrado");
        }

        Paciente paciente = pacienteOpt.get();

        if (paciente.getStatus() == Paciente.Status.SUSPENDED) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.toString(), "Paciente ya suspendido");
        }

        paciente.setStatus(Paciente.Status.SUSPENDED);
        ipaciente.save(paciente);

        return new ResponseDTO(HttpStatus.OK.toString(), "Paciente suspendido con exito");
    }

    public PacienteDTO convertToDTO(Paciente paciente) {
        return new PacienteDTO(
                paciente.getId(),
                paciente.getNombre(),
                paciente.getEmail(),
                paciente.getStatus() != null ? paciente.getStatus().name() : null,
                paciente.getRegistrationDate());
    }

    private Paciente convertToEntity(PacienteDTO pacienteDTO) {
        Paciente paciente = new Paciente();
        paciente.setNombre(pacienteDTO.getNombre());
        paciente.setEmail(pacienteDTO.getEmail());
        return paciente;
    }
}
