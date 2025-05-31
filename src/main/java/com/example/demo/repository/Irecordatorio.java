package com.example.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Recordatorio;

public interface Irecordatorio extends JpaRepository<Recordatorio, Integer> {
    List<Recordatorio> findBySuspendedFalse();
}
