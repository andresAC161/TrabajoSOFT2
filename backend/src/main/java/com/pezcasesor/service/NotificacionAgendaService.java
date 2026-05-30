package com.pezcasesor.service;

import com.pezcasesor.model.TareaEstado;
import com.pezcasesor.repository.TareaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacionAgendaService {

    private final TareaRepository tareaRepository;
    private final List<TareaVencimientoObserver> observers;

    public NotificacionAgendaService(TareaRepository tareaRepository,
                                     List<TareaVencimientoObserver> observers) {
        this.tareaRepository = tareaRepository;
        this.observers = observers;
    }

    @Transactional
    public void procesarTareasProximas() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime limite = ahora.plusMinutes(15);
        var proximas = tareaRepository.findByEstadoAndNotificadoFalseAndFechaHoraBetween(
            TareaEstado.pendiente, ahora, limite);
        proximas.forEach(tarea -> observers.forEach(obs -> obs.onTareaProxima(tarea)));
    }
}
