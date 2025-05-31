package com.example.demo.model;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nombre;
    private String email;
   
    // No se que es esto
    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDate registrationDate;

    public enum Status {
        ACTIVE,
        SUSPENDED
    }

    @PrePersist
    public void prePersist() {
        if (registrationDate == null) {
            registrationDate = LocalDate.now();
        }
    }
}
