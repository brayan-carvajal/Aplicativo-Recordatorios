package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmarDTO {
    private Long id;
    private int idPaciente;
    private int idMecamento;
    private LocalDateTime confirmadoHora;
    private boolean confirmado;
}
