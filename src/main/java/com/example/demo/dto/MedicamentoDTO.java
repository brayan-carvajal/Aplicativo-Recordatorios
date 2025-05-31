package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicamentoDTO {
    private int id;
    private String nombre;
    private String dosis;
    private int frecuenciaHoras;
}
