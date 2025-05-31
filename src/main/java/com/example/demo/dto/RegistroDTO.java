package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroDTO {
    private int id;
    private Integer idPaciente;
    private Integer idMecamento;
    private LocalDateTime envioHora;
    private String mensaje;
    public Integer getIdRecordatorio() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getIdRecordatorio'");
    }
}
