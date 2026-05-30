package com.pezcasesor.repository;

import com.pezcasesor.model.Tarea;
import com.pezcasesor.model.TareaEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {
    List<Tarea> findByUsuarioId(Long usuarioId);
    List<Tarea> findByEstadoAndNotificadoFalseAndFechaHoraBetween(
        TareaEstado estado, LocalDateTime desde, LocalDateTime hasta);
}
