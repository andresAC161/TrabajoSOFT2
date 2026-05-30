package com.pezcasesor.service;

import com.pezcasesor.model.LoteRegistroDTO;
import com.pezcasesor.model.LoteRespuestaDTO;
import com.pezcasesor.model.Lote;
import com.pezcasesor.model.LoteEstado;
import com.pezcasesor.repository.LoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoteService {

    private final LoteRepository loteRepository;
    private final EstanqueService estanqueService;

    public LoteService(LoteRepository loteRepository, EstanqueService estanqueService) {
        this.loteRepository = loteRepository;
        this.estanqueService = estanqueService;
    }

    @Transactional
    public LoteRespuestaDTO registrar(LoteRegistroDTO dto) {
        if (dto.getCantidad() == null || dto.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
        }
        if (dto.getPesoInicialG() == null || dto.getPesoInicialG().signum() <= 0) {
            throw new IllegalArgumentException("El peso inicial debe ser mayor a 0.");
        }
        String estado = estanqueService.buscarPorId(dto.getEstanqueId()).getEstado();
        if (!"disponible".equals(estado)) {
            throw new IllegalStateException("El estanque no está disponible para registrar un lote.");
        }
        Lote lote = new Lote();
        lote.setEstanqueId(dto.getEstanqueId());
        lote.setEspecie(dto.getEspecie().trim());
        lote.setCantidad(dto.getCantidad());
        lote.setFechaSiembra(dto.getFechaSiembra());
        lote.setPesoInicialG(dto.getPesoInicialG());
        lote.setEstado(LoteEstado.activo);
        Lote guardado = loteRepository.save(lote);
        estanqueService.ocupar(dto.getEstanqueId());
        return toDTO(guardado);
    }

    @Transactional
    public LoteRespuestaDTO finalizar(Long loteId) {
        Lote lote = loteRepository.findById(loteId)
            .orElseThrow(() -> new IllegalArgumentException("Lote no encontrado."));
        if (lote.getEstado() == LoteEstado.finalizado) {
            throw new IllegalStateException("El lote ya está finalizado.");
        }
        lote.setEstado(LoteEstado.finalizado);
        lote.setFechaFin(LocalDate.now());
        loteRepository.save(lote);
        estanqueService.liberar(lote.getEstanqueId());
        return toDTO(lote);
    }

    public List<LoteRespuestaDTO> listarPorEstanque(Long estanqueId) {
        return loteRepository.findByEstanqueId(estanqueId)
            .stream().map(this::toDTO).collect(Collectors.toList());
    }

    private LoteRespuestaDTO toDTO(Lote l) {
        return new LoteRespuestaDTO(
            l.getId(), l.getEstanqueId(), l.getEspecie(), l.getCantidad(),
            l.getFechaSiembra(), l.getPesoInicialG(), l.getEstado().name(), l.getFechaFin()
        );
    }
}
