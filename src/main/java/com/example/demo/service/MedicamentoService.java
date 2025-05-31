package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.demo.dto.MedicamentoDTO;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.model.Medicamento;
import com.example.demo.repository.Imedicamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class MedicamentoService {

    @Autowired
    private Imedicamento imedicamento;

    // Crear
    public ResponseDTO createMedicamento(MedicamentoDTO medicamentoDTO) {
        if (medicamentoDTO.getNombre() == null || medicamentoDTO.getNombre().trim().isEmpty()) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.toString(), "Nombre del medicamento requerido");
        }

        Medicamento medicamento = convertToEntity(medicamentoDTO);
        imedicamento.save(medicamento);
        return new ResponseDTO(HttpStatus.CREATED.toString(), "Medicamento guardado correctamente");
    }

    // Obtener todos
    public List<MedicamentoDTO> getAllMedicamentos() {
        List<Medicamento> medicamentos = imedicamento.findAll();
        return medicamentos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Obtener por id
    public Optional<MedicamentoDTO> getMedicamentoById(int id) {
        return imedicamento.findById(id).map(this::convertToDTO);
    }

    // Actualizar
    public ResponseDTO updateMedicamento(int id, MedicamentoDTO updateMedicamentoDTO) {
        Optional<Medicamento> medicamentoOpt = imedicamento.findById(id);
        if (medicamentoOpt.isEmpty()) {
            return new ResponseDTO(HttpStatus.NOT_FOUND.toString(), "Medicamento no encontrado");
        }

        Medicamento medicamento = medicamentoOpt.get();

        if (updateMedicamentoDTO.getNombre() != null && !updateMedicamentoDTO.getNombre().trim().isEmpty()) {
            medicamento.setNombre(updateMedicamentoDTO.getNombre());
        }
        if (updateMedicamentoDTO.getDosis() != null)
            medicamento.setDosis(updateMedicamentoDTO.getDosis());
        if (updateMedicamentoDTO.getFrecuenciaHoras() != 0)
            medicamento.setFrecuenciaHoras(updateMedicamentoDTO.getFrecuenciaHoras());

        imedicamento.save(medicamento);
        return new ResponseDTO(HttpStatus.OK.toString(), "Medicamento actualizado con éxito");
    }

    // Eliminar
    public ResponseDTO deleteMedicamento(int id) {
        Optional<Medicamento> medicamentoOpt = imedicamento.findById(id);
        if (medicamentoOpt.isEmpty()) {
            return new ResponseDTO(HttpStatus.NOT_FOUND.toString(), "Medicamento no encontrado");
        }

        imedicamento.deleteById(id);
        return new ResponseDTO(HttpStatus.OK.toString(), "Medicamento eliminado con éxito");
    }

    public MedicamentoDTO convertToDTO(Medicamento medicamento) {
        return new MedicamentoDTO(
                medicamento.getId(),
                medicamento.getNombre(),
                medicamento.getDosis(),
                medicamento.getFrecuenciaHoras());
    }

    private Medicamento convertToEntity(MedicamentoDTO medicamentoDTO) {
        Medicamento medicamento = new Medicamento();
        medicamento.setNombre(medicamentoDTO.getNombre());
        medicamento.setDosis(medicamentoDTO.getDosis());
        medicamento.setFrecuenciaHoras(medicamentoDTO.getFrecuenciaHoras());
        return medicamento;
    }
}
