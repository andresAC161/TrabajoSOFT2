package com.pezcasesor.service;

import com.pezcasesor.model.Notificacion;
import com.pezcasesor.model.Tarea;
import com.pezcasesor.repository.NotificacionRepository;
import com.pezcasesor.repository.TareaRepository;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class NotificacionCreadorObserver implements TareaVencimientoObserver {

    private final NotificacionRepository notificacionRepository;
    private final TareaRepository tareaRepository;

    public NotificacionCreadorObserver(NotificacionRepository notificacionRepository,
                                       TareaRepository tareaRepository) {
        this.notificacionRepository = notificacionRepository;
        this.tareaRepository = tareaRepository;
    }

    @Override
    public void onTareaProxima(Tarea tarea) {
        String mensaje = "Recordatorio: \"" + tarea.getNombre() + "\" programada para "
            + tarea.getFechaHora().toLocalDate()
            + " a las " + tarea.getFechaHora().toLocalTime().toString().substring(0, 5) + ".";
        Notificacion n = new Notificacion();
        n.setTareaId(tarea.getId());
        n.setUsuarioId(tarea.getUsuarioId());
        n.setTipo("recordatorio");
        n.setMensaje(mensaje);
        n.setLeida(false);
        n.setFechaEnvio(LocalDateTime.now());
        notificacionRepository.save(n);
        tarea.setNotificado(true);
        tareaRepository.save(tarea);
    }
}
