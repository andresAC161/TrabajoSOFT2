package com.pezcasesor.service;

import com.pezcasesor.model.ConsultaGuia;
import com.pezcasesor.model.Guia;
import com.pezcasesor.model.GuiaRespuestaDTO;
import com.pezcasesor.model.Lote;
import com.pezcasesor.model.ParametroAgua;
import com.pezcasesor.model.UmbralesAlerta;
import com.pezcasesor.repository.ConsultaGuiaRepository;
import com.pezcasesor.repository.GuiaRepository;
import com.pezcasesor.repository.LoteRepository;
import com.pezcasesor.repository.ParametroAguaRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GuiaService {

    private final GuiaRepository guiaRepository;
    private final ConsultaGuiaRepository consultaGuiaRepository;
    private final LoteRepository loteRepository;
    private final ParametroAguaRepository parametroAguaRepository;

    public GuiaService(GuiaRepository guiaRepository, ConsultaGuiaRepository consultaGuiaRepository,
                        LoteRepository loteRepository, ParametroAguaRepository parametroAguaRepository) {
        this.guiaRepository = guiaRepository;
        this.consultaGuiaRepository = consultaGuiaRepository;
        this.loteRepository = loteRepository;
        this.parametroAguaRepository = parametroAguaRepository;
    }

    public List<GuiaRespuestaDTO> listarPorEspecie(String especie) {
        return guiaRepository.findByEspecieIgnoreCase(especie)
            .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<GuiaRespuestaDTO> recomendarPorLote(Long loteId) {
        Lote lote = loteRepository.findById(loteId)
            .orElseThrow(() -> new IllegalArgumentException("Lote no encontrado."));
        List<String> parametrosAnomalos = parametroAguaRepository
            .findFirstByEstanqueIdOrderByFechaRegistroDesc(lote.getEstanqueId())
            .map(p -> parametrosAnomalos(p, lote.getEspecie()))
            .orElse(List.of());

        List<Guia> guias = parametrosAnomalos.isEmpty()
            ? guiaRepository.findByEspecieIgnoreCaseAndCategoria(lote.getEspecie(), "alimentacion")
            : guiaRepository.findByEspecieIgnoreCaseAndCategoriaAndParametroIn(
                lote.getEspecie(), "sanidad", parametrosAnomalos);

        for (Guia guia : guias) {
            ConsultaGuia consulta = new ConsultaGuia();
            consulta.setGuiaId(guia.getId());
            consulta.setLoteId(loteId);
            consulta.setFechaConsulta(LocalDateTime.now());
            consultaGuiaRepository.save(consulta);
        }
        return guias.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private List<String> parametrosAnomalos(ParametroAgua p, String especie) {
        List<String> anomalos = new ArrayList<>();
        if (UmbralesAlerta.phFueraDeRango(especie, p.getPh().doubleValue()))
            anomalos.add("ph");
        if (UmbralesAlerta.temperaturaFueraDeRango(especie, p.getTemperaturaC().doubleValue()))
            anomalos.add("temperatura");
        if (UmbralesAlerta.oxigenoFueraDeRango(especie, p.getOxigenoMgl().doubleValue()))
            anomalos.add("oxigeno");
        if (UmbralesAlerta.amoniacoFueraDeRango(especie, p.getAmoniacoMgl().doubleValue()))
            anomalos.add("amoniaco");
        return anomalos;
    }

    private GuiaRespuestaDTO toDTO(Guia g) {
        return new GuiaRespuestaDTO(g.getId(), g.getEspecie(), g.getCategoria(), g.getParametro(),
            g.getTitulo(), g.getContenido());
    }
}
