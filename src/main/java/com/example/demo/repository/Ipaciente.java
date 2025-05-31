package com.example.demo.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Paciente;

public interface Ipaciente extends JpaRepository<Paciente, Integer> {
    List<Paciente> findByStatus(Paciente.Status status);
    Optional<Paciente> findByIdAndStatus(int id, Paciente.Status status);
}
