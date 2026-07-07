package com.pezcasesor.service;

import com.pezcasesor.model.ParametroAguaRegistroDTO;
import com.pezcasesor.model.ParametroAguaRespuestaDTO;
import com.pezcasesor.model.ParametroAgua;
import com.pezcasesor.model.Alerta;
import com.pezcasesor.model.UmbralesAlerta;
import com.pezcasesor.model.LoteEstado;
import com.pezcasesor.repository.ParametroAguaRepository;
import com.pezcasesor.repository.LoteRepository;
import com.pezcasesor.repository.AlertaRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParametroAguaService {

    private final ParametroAguaRepository parametroAguaRepository;
    private final LoteRepository loteRepository;
    private final AlertaRepository alertaRepository;

    public ParametroAguaService(ParametroAguaRepository parametroAguaRepository,
                                 LoteRepository loteRepository,
                                 AlertaRepository alertaRepository) {
        this.parametroAguaRepository = parametroAguaRepository;
        this.loteRepository = loteRepository;
        this.alertaRepository = alertaRepository;
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
        ParametroAgua guardado = parametroAguaRepository.save(p);
        List<String> alertas = detectarAnomalias(dto, guardado);
        return toDTO(guardado, alertas);
    }

    private List<String> detectarAnomalias(ParametroAguaRegistroDTO dto, ParametroAgua parametro) {
        List<String> mensajes = new ArrayList<>();
        loteRepository.findByEstanqueId(dto.getEstanqueId()).stream()
            .filter(l -> l.getEstado() == LoteEstado.activo)
            .findFirst()
            .ifPresent(lote -> {
                String especie = lote.getEspecie();
                double ph = dto.getPh().doubleValue();
                double temperatura = dto.getTemperaturaC().doubleValue();
                double oxigeno = dto.getOxigenoMgl().doubleValue();
                double amoniaco = dto.getAmoniacoMgl().doubleValue();
                if (UmbralesAlerta.phFueraDeRango(especie, ph))
                    mensajes.add("pH fuera de rango para " + especie);
                if (UmbralesAlerta.temperaturaFueraDeRango(especie, temperatura))
                    mensajes.add("Temperatura fuera de rango para " + especie);
                if (UmbralesAlerta.oxigenoFueraDeRango(especie, oxigeno))
                    mensajes.add("Oxígeno disuelto por debajo del mínimo para " + especie);
                if (UmbralesAlerta.amoniacoFueraDeRango(especie, amoniaco))
                    mensajes.add("Amoníaco por encima del máximo seguro para " + especie);
            });
        for (String mensaje : mensajes) {
            Alerta alerta = new Alerta();
            alerta.setEstanqueId(dto.getEstanqueId());
            alerta.setParametroId(parametro.getId());
            alerta.setMensaje(mensaje);
            alerta.setFechaRegistro(LocalDateTime.now());
            alertaRepository.save(alerta);
        }
        return mensajes;
    }

    public List<ParametroAguaRespuestaDTO> listarPorEstanque(Long estanqueId) {
        return parametroAguaRepository.findByEstanqueIdOrderByFechaRegistroDesc(estanqueId)
            .stream().map(p -> toDTO(p, Collections.emptyList())).collect(Collectors.toList());
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

    private ParametroAguaRespuestaDTO toDTO(ParametroAgua p, List<String> alertas) {
        return new ParametroAguaRespuestaDTO(
            p.getId(), p.getEstanqueId(), p.getUsuarioId(),
            p.getPh(), p.getTemperaturaC(), p.getOxigenoMgl(),
            p.getAmoniacoMgl(), p.getFechaRegistro(), alertas
        );
    }
}
