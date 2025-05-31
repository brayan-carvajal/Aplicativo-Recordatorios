package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Registro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idRecordatorio", nullable = false)
    private Recordatorio recordatorio;

    @Column(nullable = false)
    private LocalDateTime envioHora;

    @Column(nullable = false)
    private boolean confirmado;

    private LocalDateTime confirmarHora;

    @Column(unique = true)
    private String confirmarToken;

    @Column(nullable = true) 
    private LocalDate fechaRegistro;

    @PrePersist
    protected void onCreate() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDate.now();
        }
        if (envioHora == null) {
            envioHora = LocalDateTime.now();
        }
    }
}