package com.pezcasesor.service;

import com.pezcasesor.model.ParametroAguaRegistroDTO;
import com.pezcasesor.model.ParametroAguaRespuestaDTO;
import com.pezcasesor.model.ParametroAgua;
import com.pezcasesor.repository.ParametroAguaRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParametroAguaService {

    private final ParametroAguaRepository parametroAguaRepository;

    public ParametroAguaService(ParametroAguaRepository parametroAguaRepository) {
        this.parametroAguaRepository = parametroAguaRepository;
    }

    public ParametroAguaRespuestaDTO registrar(ParametroAguaRegistroDTO dto) {
        validar(dto);
        ParametroAgua p = new ParametroAgua();
        p.setEstanqueId(dto.getEstanqueId());
        p.setUsuarioId(dto.getUsuarioId());
        p.setPh(dto.getPh());
        p.setTemperaturaC(dto.getTemperaturaC());
        p.setOxigenoMgl(dto.getOxigenoMgl());
        p.setAmoniacoMgl(dto.getAmoniacoMgl());
        p.setFechaRegistro(LocalDateTime.now());
        return toDTO(parametroAguaRepository.save(p));
    }

    public List<ParametroAguaRespuestaDTO> listarPorEstanque(Long estanqueId) {
        return parametroAguaRepository.findByEstanqueIdOrderByFechaRegistroDesc(estanqueId)
            .stream().map(this::toDTO).collect(Collectors.toList());
    }

    private void validar(ParametroAguaRegistroDTO dto) {
        if (dto.getPh() == null || dto.getPh().compareTo(new BigDecimal("0.1")) < 0
                || dto.getPh().compareTo(new BigDecimal("14.0")) > 0) {
            throw new IllegalArgumentException("pH debe estar entre 0.1 y 14.0.");
        }
        if (dto.getTemperaturaC() == null || dto.getTemperaturaC().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La temperatura debe ser mayor a 0.");
        }
        if (dto.getOxigenoMgl() == null || dto.getOxigenoMgl().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El oxígeno disuelto debe ser mayor a 0.");
        }
        if (dto.getAmoniacoMgl() == null || dto.getAmoniacoMgl().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El amoniaco debe ser mayor a 0.");
        }
    }

    private ParametroAguaRespuestaDTO toDTO(ParametroAgua p) {
        return new ParametroAguaRespuestaDTO(
            p.getId(), p.getEstanqueId(), p.getUsuarioId(),
            p.getPh(), p.getTemperaturaC(), p.getOxigenoMgl(),
            p.getAmoniacoMgl(), p.getFechaRegistro()
        );
    }
}
