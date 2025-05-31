package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordatorioDTO {

    private Integer id;
    private Integer idPaciente;
    private Integer idMedicamento;
    private LocalTime horaInicio;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}
