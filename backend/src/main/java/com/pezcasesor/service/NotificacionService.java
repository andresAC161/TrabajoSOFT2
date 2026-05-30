package com.pezcasesor.service;

import com.pezcasesor.model.NotificacionRespuestaDTO;
import com.pezcasesor.model.Notificacion;
import com.pezcasesor.repository.NotificacionRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    public NotificacionService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    public List<NotificacionRespuestaDTO> listarPorUsuario(Long usuarioId) {
        return notificacionRepository.findByUsuarioIdOrderByFechaEnvioDesc(usuarioId)
            .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public NotificacionRespuestaDTO marcarLeida(Long id) {
        Notificacion n = notificacionRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Notificación no encontrada."));
        n.setLeida(true);
        return toDTO(notificacionRepository.save(n));
    }

    private NotificacionRespuestaDTO toDTO(Notificacion n) {
        return new NotificacionRespuestaDTO(
            n.getId(), n.getTareaId(), n.getUsuarioId(),
            n.getTipo(), n.getMensaje(), n.isLeida(), n.getFechaEnvio()
        );
    }
}
