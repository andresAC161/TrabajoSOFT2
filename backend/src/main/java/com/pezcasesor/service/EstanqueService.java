package com.pezcasesor.service;

import com.pezcasesor.model.EstanqueEditarDTO;
import com.pezcasesor.model.EstanqueRegistroDTO;
import com.pezcasesor.model.EstanqueRespuestaDTO;
import com.pezcasesor.model.Estanque;
import com.pezcasesor.model.EstanqueEstado;
import com.pezcasesor.model.LoteEstado;
import com.pezcasesor.repository.EstanqueRepository;
import com.pezcasesor.repository.LoteRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstanqueService {

    private final EstanqueRepository estanqueRepository;
    private final LoteRepository loteRepository;

    public EstanqueService(EstanqueRepository estanqueRepository, LoteRepository loteRepository) {
        this.estanqueRepository = estanqueRepository;
        this.loteRepository = loteRepository;
    }

    public EstanqueRespuestaDTO registrar(EstanqueRegistroDTO dto) {
        if (dto.getCapacidadLitros() == null || dto.getCapacidadLitros().signum() <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor a 0.");
        }
        Estanque estanque = new Estanque();
        estanque.setUsuarioId(dto.getUsuarioId());
        estanque.setNombre(dto.getNombre().trim());
        estanque.setTipoAgua(dto.getTipoAgua());
        estanque.setCapacidadLitros(dto.getCapacidadLitros());
        estanque.setLocalizacion(dto.getLocalizacion());
        estanque.setEstado(EstanqueEstado.disponible);
        estanque.setFechaCreacion(LocalDateTime.now());
        return toDTO(estanqueRepository.save(estanque));
    }

    public EstanqueRespuestaDTO editar(Long id, EstanqueEditarDTO dto) {
        Estanque estanque = estanqueRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Estanque no encontrado."));
        if (dto.getCapacidadLitros() != null && dto.getCapacidadLitros().signum() <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor a 0.");
        }
        if (dto.getNombre() != null)          estanque.setNombre(dto.getNombre().trim());
        if (dto.getTipoAgua() != null)        estanque.setTipoAgua(dto.getTipoAgua());
        if (dto.getCapacidadLitros() != null) estanque.setCapacidadLitros(dto.getCapacidadLitros());
        if (dto.getLocalizacion() != null)    estanque.setLocalizacion(dto.getLocalizacion());
        return toDTO(estanqueRepository.save(estanque));
    }

    public void eliminar(Long id) {
        if (!estanqueRepository.existsById(id)) {
            throw new IllegalArgumentException("Estanque no encontrado.");
        }
        if (loteRepository.existsByEstanqueIdAndEstado(id, LoteEstado.activo)) {
            throw new IllegalStateException("No se puede eliminar: el estanque tiene un lote activo.");
        }
        estanqueRepository.deleteById(id);
    }

    public void ocupar(Long estanqueId) {
        Estanque estanque = estanqueRepository.findById(estanqueId)
            .orElseThrow(() -> new IllegalArgumentException("Estanque no encontrado."));
        estanque.setEstado(estanque.getEstado().ocupar());
        estanqueRepository.save(estanque);
    }

    public void liberar(Long estanqueId) {
        Estanque estanque = estanqueRepository.findById(estanqueId)
            .orElseThrow(() -> new IllegalArgumentException("Estanque no encontrado."));
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
