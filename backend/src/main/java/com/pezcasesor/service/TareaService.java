package com.pezcasesor.service;

import com.pezcasesor.model.TareaEditarDTO;
import com.pezcasesor.model.TareaRegistroDTO;
import com.pezcasesor.model.TareaRespuestaDTO;
import com.pezcasesor.model.Tarea;
import com.pezcasesor.model.TareaEstado;
import com.pezcasesor.repository.TareaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TareaService {

    private final TareaRepository tareaRepository;

    public TareaService(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    public TareaRespuestaDTO crear(TareaRegistroDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la tarea es obligatorio.");
        }
        if (dto.getFechaHora() == null) {
            throw new IllegalArgumentException("La fecha y hora son obligatorias.");
        }
        Tarea tarea = new Tarea();
        tarea.setUsuarioId(dto.getUsuarioId());
        tarea.setEstanqueId(dto.getEstanqueId());
        tarea.setLoteId(dto.getLoteId());
        tarea.setNombre(dto.getNombre().trim());
        tarea.setDescripcion(dto.getDescripcion());
        tarea.setFechaHora(dto.getFechaHora());
        tarea.setEstado(TareaEstado.pendiente);
        tarea.setNotificado(false);
        return toDTO(tareaRepository.save(tarea));
    }

    public TareaRespuestaDTO actualizar(Long id, TareaEditarDTO dto) {
        Tarea tarea = tareaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada."));
        if (dto.getNombre() != null)      tarea.setNombre(dto.getNombre().trim());
        if (dto.getDescripcion() != null) tarea.setDescripcion(dto.getDescripcion());
        if (dto.getFechaHora() != null)   tarea.setFechaHora(dto.getFechaHora());
        if (dto.getEstado() != null) {
            switch (dto.getEstado()) {
                case "completada" -> tarea.setEstado(tarea.getEstado().completar());
                case "cancelada"  -> tarea.setEstado(tarea.getEstado().cancelar());
                default -> throw new IllegalArgumentException("Estado no reconocido: " + dto.getEstado());
            }
        }
        return toDTO(tareaRepository.save(tarea));
    }

    public List<TareaRespuestaDTO> listarPorUsuario(Long usuarioId) {
        return tareaRepository.findByUsuarioId(usuarioId)
            .stream().map(this::toDTO).collect(Collectors.toList());
    }
    
    public List<TareaRespuestaDTO> listarPorUsuarioYEstado(Long usuarioId, String estadoTexto) {
        TareaEstado estado;
        try {
            estado = TareaEstado.valueOf(estadoTexto.toLowerCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado no válido: " + estadoTexto);
        }
        return tareaRepository.findByUsuarioIdAndEstado(usuarioId, estado)
            .stream().map(this::toDTO).collect(Collectors.toList());
    }

    TareaRespuestaDTO toDTO(Tarea t) {
        return new TareaRespuestaDTO(
            t.getId(), t.getUsuarioId(), t.getEstanqueId(), t.getLoteId(),
            t.getNombre(), t.getDescripcion(), t.getFechaHora(),
            t.getEstado().name(), t.isNotificado()
        );
    }
}
