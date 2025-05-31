package com.example.demo.repository;

import com.example.demo.model.Registro;
import com.example.demo.model.Recordatorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface Iregistro extends JpaRepository<Registro, Integer> {

    // Verifica si ya se envió un recordatorio en una hora específica
    boolean existsByRecordatorioAndEnvioHora(Recordatorio recordatorio, LocalDateTime envioHora);

    // Encuentra el último registro de un recordatorio
    Optional<Registro> findTopByRecordatorio_IdOrderByEnvioHoraDesc(Integer recordatorioId);

    // Buscar por token de confirmación
    Optional<Registro> findByConfirmarToken(String confirmarToken);

    // Verifica si ya fue confirmado en un rango de tiempo (por ejemplo, para un
    // día)
    boolean existsByRecordatorioAndConfirmadoTrueAndConfirmarHoraBetween(
            Recordatorio recordatorio, LocalDateTime startOfDay, LocalDateTime endOfDay);

    // Encuentra el registro más reciente confirmado
    @Query("SELECT r FROM Registro r WHERE r.recordatorio.id = :recordatorioId AND r.confirmado = true ORDER BY r.envioHora DESC")
    Optional<Registro> findLatestConfirmedByRecordatorioId(@Param("recordatorioId") Integer recordatorioId);

    // Todos los registros confirmados de una fecha específica
    @Query("SELECT r FROM Registro r WHERE r.fechaRegistro = :date AND r.confirmado = true")
    List<Registro> findByConfirmadoTrueAndFechaRegistro(@Param("date") LocalDate date);

    // Verifica si existe un log para una asignación en una fecha específica
    boolean existsByRecordatorioAndFechaRegistro(Recordatorio recordatorio, LocalDate date);

    // Encuentra registros por fecha (por ejemplo, para reinicio diario)
    List<Registro> findByFechaRegistro(LocalDate date);

    // Último registro no confirmado para hoy
    @Query("SELECT r FROM Registro r WHERE r.recordatorio.id = :recordatorioId AND r.fechaRegistro = CURRENT_DATE AND r.confirmado = false ORDER BY r.envioHora DESC")
    Optional<Registro> findTodayUnconfirmedByRecordatorioId(@Param("recordatorioId") Integer recordatorioId);
}
