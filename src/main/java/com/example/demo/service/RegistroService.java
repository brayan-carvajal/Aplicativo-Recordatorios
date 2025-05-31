package com.example.demo.service;

import com.example.demo.dto.RegistroDTO;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.model.Registro;
import com.example.demo.repository.Iregistro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RegistroService {

    @Autowired
    private Iregistro iregistro;

    // Crear Registro
    public ResponseDTO createRegistro(RegistroDTO dto) {
        try {
            Registro registro = convertToEntity(dto);
            iregistro.save(registro);
            return new ResponseDTO(HttpStatus.OK.toString(), "Registro creado correctamente");
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.toString(), "Error al crear registro: " + e.getMessage());
        }
    }

    // Obtener todos los registros
    public List<RegistroDTO> getAllRegistros() {
        List<Registro> registros = iregistro.findAll();
        return registros.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Obtener por ID
    public Optional<RegistroDTO> getRegistroById(int id) {
        Optional<Registro> registroOpt = iregistro.findById(id);
        return registroOpt.map(this::convertToDTO);
    }

    // Eliminar
    public ResponseDTO deleteRegistro(int id) {
        Optional<Registro> registroOpt = iregistro.findById(id);
        if (registroOpt.isEmpty()) {
            return new ResponseDTO(HttpStatus.NOT_FOUND.toString(), "Registro no encontrado");
        }
        iregistro.deleteById(id);
        return new ResponseDTO(HttpStatus.OK.toString(), "Registro eliminado correctamente");
    }

    // Conversión de entidad a DTO
    private RegistroDTO convertToDTO(Registro registro) {
        return new RegistroDTO(
                registro.getId(),
                registro.getRecordatorio() != null ? registro.getRecordatorio().getPaciente().getId() : null,
                registro.getRecordatorio() != null ? registro.getRecordatorio().getMedicamento().getId() : null,
                registro.getEnvioHora(),
                registro.isConfirmado() ? "Confirmado" : "Pendiente");
    }

    // Conversión de DTO a entidad
    private Registro convertToEntity(RegistroDTO dto) {
        Registro registro = new Registro();
        registro.setId(dto.getId());

        registro.setRecordatorio(null);

        registro.setEnvioHora(dto.getEnvioHora());
        registro.setConfirmado("Confirmado".equalsIgnoreCase(dto.getMensaje()));

        return registro;
    }
}
