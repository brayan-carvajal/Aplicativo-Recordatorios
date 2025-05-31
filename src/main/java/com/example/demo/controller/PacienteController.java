package com.example.demo.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.PacienteDTO;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.service.PacienteService;

@RestController
@RequestMapping("/api/paciente")
@CrossOrigin(origins = "*")
public class PacienteController {

    private PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    // POST
    @PostMapping("/post")
    public ResponseEntity<ResponseDTO> createPaciente(@RequestBody PacienteDTO pacienteDTO) {
        ResponseDTO response = pacienteService.createPaciente(pacienteDTO);
        if (response.getStatus().equals("200")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(400).body(response);
        }
    }

    // GET
    @GetMapping("/get")
    public ResponseEntity<List<PacienteDTO>> getAllPacientes() {
        List<PacienteDTO> pacientes = pacienteService.getAllPacientes();
        return ResponseEntity.ok(pacientes);
    }

    // GET:id
    @GetMapping("/{id}")
    public ResponseEntity<?> getPacienteById(@PathVariable int id) {
        Optional<PacienteDTO> pacienteOpt = pacienteService.getPacienteById(id);
        if (pacienteOpt.isPresent()) {
            return ResponseEntity.ok(pacienteOpt.get());
        }
        return ResponseEntity.status(404).body(new ResponseDTO("404", "Paciente no encontrado"));
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updatePaciente(@PathVariable int id, @RequestBody PacienteDTO pacienteDTO) {
        ResponseDTO response = pacienteService.updatePaciente(id, pacienteDTO);
        if (response.getStatus().equals("200")) {
            return ResponseEntity.ok(response);
        } else if (response.getStatus().equals("404")) {
            return ResponseEntity.status(404).body(response);
        } else {
            return ResponseEntity.status(400).body(response);
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deletePaciente(@PathVariable int id) {
        ResponseDTO response = pacienteService.deletePaciente(id);
        if (response.getStatus().equals("200")) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(404).body(response);
        }
    }
}
