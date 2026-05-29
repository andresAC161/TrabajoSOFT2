package com.pezcasesor.service;

import com.pezcasesor.model.EstanqueRegistroDTO;
import com.pezcasesor.model.EstanqueRespuestaDTO;
import com.pezcasesor.model.Estanque;
import com.pezcasesor.model.EstanqueEstado;
import com.pezcasesor.repository.EstanqueRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstanqueService {

    private final EstanqueRepository estanqueRepository;

    public EstanqueService(EstanqueRepository estanqueRepository) {
        this.estanqueRepository = estanqueRepository;
    }

    public EstanqueRespuestaDTO registrar(EstanqueRegistroDTO dto) {
        if (dto.getCapacidadLitros() == null || dto.getCapacidadLitros().signum() <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor a 0.");
        }
        // PATRÓN: Factory Method — crea Estanque con estado inicial disponible
        Estanque estanque = new Estanque();
        estanque.setUsuarioId(dto.getUsuarioId());
        estanque.setNombre(dto.getNombre().trim());
        estanque.setTipoAgua(dto.getTipoAgua());
        estanque.setCapacidadLitros(dto.getCapacidadLitros());
        estanque.setLocalizacion(dto.getLocalizacion());
        // PATRÓN: State — el estanque inicia en estado disponible
        estanque.setEstado(EstanqueEstado.disponible);
        estanque.setFechaCreacion(LocalDateTime.now());
        return toDTO(estanqueRepository.save(estanque));
    }

    public void ocupar(Long estanqueId) {
        Estanque estanque = estanqueRepository.findById(estanqueId)
            .orElseThrow(() -> new IllegalArgumentException("Estanque no encontrado."));
        // PATRÓN: State — transición disponible → ocupado
        estanque.setEstado(estanque.getEstado().ocupar());
        estanqueRepository.save(estanque);
    }

    public void liberar(Long estanqueId) {
        Estanque estanque = estanqueRepository.findById(estanqueId)
            .orElseThrow(() -> new IllegalArgumentException("Estanque no encontrado."));
        // PATRÓN: State — transición ocupado → disponible
        estanque.setEstado(estanque.getEstado().liberar());
        estanqueRepository.save(estanque);
    }

    public List<EstanqueRespuestaDTO> listarPorUsuario(Long usuarioId) {
        return estanqueRepository.findByUsuarioId(usuarioId)
            .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public EstanqueRespuestaDTO buscarPorId(Long id) {
        return toDTO(estanqueRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Estanque no encontrado.")));
    }

    private EstanqueRespuestaDTO toDTO(Estanque e) {
        return new EstanqueRespuestaDTO(
            e.getId(), e.getUsuarioId(), e.getNombre(), e.getTipoAgua(),
            e.getCapacidadLitros(), e.getLocalizacion(),
            e.getEstado().name(), e.getFechaCreacion()
        );
    }
}
