package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.example.demo.dto.RecordatorioDTO;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.model.Medicamento;
import com.example.demo.model.Paciente;
import com.example.demo.model.Recordatorio;
import com.example.demo.repository.Irecordatorio;
import com.example.demo.repository.Ipaciente;
import com.example.demo.repository.Imedicamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class RecordatorioService {

    @Autowired
    private Irecordatorio irecordatorio;

    @Autowired
    private Ipaciente ipaciente;

    @Autowired
    private Imedicamento imedicamento;

    // Crear
    public ResponseDTO createRecordatorio(RecordatorioDTO recordatorioDTO) {
        try {
            Recordatorio recordatorio = convertToEntity(recordatorioDTO);
            irecordatorio.save(recordatorio);
            return new ResponseDTO(HttpStatus.OK.toString(), "Recordatorio creado correctamente");
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.toString(), "Error al crear recordatorio: " + e.getMessage());
        }
    }

    // Obtener todos
    public List<RecordatorioDTO> getAllRecordatorios() {
        List<Recordatorio> recordatorios = irecordatorio.findAll();
        return recordatorios.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Obtener por id
    public Optional<RecordatorioDTO> getRecordatorioById(int id) {
        Optional<Recordatorio> recordatorioOpt = irecordatorio.findById(id);
        return recordatorioOpt.map(this::convertToDTO);
    }

    // Actualizar
    public ResponseDTO updateRecordatorio(int id, RecordatorioDTO updateRecordatorioDTO) {
        Optional<Recordatorio> recordatorioOpt = irecordatorio.findById(id);
        if (recordatorioOpt.isEmpty()) {
            return new ResponseDTO(HttpStatus.NOT_FOUND.toString(), "No se encontra el recordatorio");
        }

        Recordatorio recordatorio = recordatorioOpt.get();

        // Actualizar paciente si existe
        Optional<Paciente> pacienteOpt = ipaciente.findById(updateRecordatorioDTO.getIdPaciente());
        if (pacienteOpt.isEmpty()) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.toString(), "Paciente no encontrado");
        }
        // Actualizar medicamento si existe
        Optional<Medicamento> medicamentoOpt = imedicamento.findById(updateRecordatorioDTO.getIdMedicamento());
        if (medicamentoOpt.isEmpty()) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.toString(), "Medicamento no encontrado");
        }
        // Actualizar todos los campos
        recordatorio.setPaciente(pacienteOpt.get());
        recordatorio.setMedicamento(medicamentoOpt.get());
        recordatorio.setHoraInicio(updateRecordatorioDTO.getHoraInicio());
        recordatorio.setFechaInicio(updateRecordatorioDTO.getFechaInicio());
        recordatorio.setFechaFin(updateRecordatorioDTO.getFechaFin());

        irecordatorio.save(recordatorio);
        return new ResponseDTO(HttpStatus.OK.toString(), "Recordatorio actualizado con éxito");
    }

    // Eliminar
    public ResponseDTO deleteRecordatorio(int id) {
        Optional<Recordatorio> recordatorioOpt = irecordatorio.findById(id);
        if (recordatorioOpt.isEmpty()) {
            return new ResponseDTO(HttpStatus.NOT_FOUND.toString(), "Recordatorio no encontrado");
        }

        irecordatorio.deleteById(id);
        return new ResponseDTO(HttpStatus.OK.toString(), "Recordatorio eliminado con exito");
    }

    public RecordatorioDTO convertToDTO(Recordatorio recordatorio) {
        return new RecordatorioDTO(
                recordatorio.getId(),
                recordatorio.getPaciente() != null ? recordatorio.getPaciente().getId() : null,
                recordatorio.getMedicamento() != null ? recordatorio.getMedicamento().getId() : null,
                recordatorio.getHoraInicio(),
                recordatorio.getFechaInicio(),
                recordatorio.getFechaFin());
    }

    private Recordatorio convertToEntity(RecordatorioDTO recordatorioDTO) throws Exception {
        Recordatorio recordatorio = new Recordatorio();

        if (recordatorioDTO.getId() != null) {
            recordatorio.setId(recordatorioDTO.getId());
        }

        Optional<Paciente> pacienteOpt = ipaciente.findById(recordatorioDTO.getIdPaciente());
        if (pacienteOpt.isEmpty()) {
            throw new Exception("Paciente no encontrado");
        }

        Optional<Medicamento> medicamentoOpt = imedicamento.findById(recordatorioDTO.getIdMedicamento());
        if (medicamentoOpt.isEmpty()) {
            throw new Exception("Medicamento no encontrado");
        }

        recordatorio.setPaciente(pacienteOpt.get());
        recordatorio.setMedicamento(medicamentoOpt.get());
        recordatorio.setHoraInicio(recordatorioDTO.getHoraInicio());
        recordatorio.setFechaInicio(recordatorioDTO.getFechaInicio());
        recordatorio.setFechaFin(recordatorioDTO.getFechaFin());

        return recordatorio;
    }
}
