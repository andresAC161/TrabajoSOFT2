package com.pezcasesor.service;

import com.pezcasesor.model.Lote;
import com.pezcasesor.model.LoteEstado;
import com.pezcasesor.model.ParametroAgua;
import com.pezcasesor.repository.LoteRepository;
import com.pezcasesor.repository.ParametroAguaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas de caja blanca / unitarias del servicio de recomendaciones.
 * Cubre la guarda de minimo de lecturas y las tendencias sostenidas.
 */
class ServicioRecomendacionesTest {

    private static final Long ESTANQUE = 1L;

    private ParametroAguaRepository parametroAguaRepository;
    private LoteRepository loteRepository;
    private ServicioRecomendaciones servicio;

    @BeforeEach
    void setUp() {
        parametroAguaRepository = mock(ParametroAguaRepository.class);
        loteRepository = mock(LoteRepository.class);
        servicio = new ServicioRecomendaciones(parametroAguaRepository, loteRepository);
    }

    private ParametroAgua lectura(double ph, double temp, double oxigeno, double amoniaco) {
        ParametroAgua p = new ParametroAgua();
        p.setPh(BigDecimal.valueOf(ph));
        p.setTemperaturaC(BigDecimal.valueOf(temp));
        p.setOxigenoMgl(BigDecimal.valueOf(oxigeno));
        p.setAmoniacoMgl(BigDecimal.valueOf(amoniaco));
        return p;
    }

    private void stubHistorico(List<ParametroAgua> lecturas) {
        when(parametroAguaRepository.findByEstanqueIdOrderByFechaRegistroDesc(ESTANQUE))
            .thenReturn(lecturas);
    }

    @Test
    void menosDeTresLecturas_noGeneraConsejos() {
        stubHistorico(List.of(lectura(7, 14, 8, 0.01), lectura(7, 14, 8, 0.01)));
        assertTrue(servicio.evaluarTendencias(ESTANQUE).isEmpty());
    }

    @Test
    void oxigenoSostenidoALaBaja_generaConsejoDeAireacion() {
        stubHistorico(List.of(
            lectura(7, 14, 5.0, 0.01),
            lectura(7, 14, 6.0, 0.01),
            lectura(7, 14, 7.0, 0.01)));
        when(loteRepository.findByEstanqueId(ESTANQUE)).thenReturn(List.of());

        List<String> consejos = servicio.evaluarTendencias(ESTANQUE);

        assertTrue(consejos.contains("Aumentar aireación nocturna"));
    }

    @Test
    void amoniacoSostenidoAlAlza_generaConsejoDeRecambio() {
        stubHistorico(List.of(
            lectura(7, 14, 8, 0.9),
            lectura(7, 14, 8, 0.5),
            lectura(7, 14, 8, 0.3)));
        when(loteRepository.findByEstanqueId(ESTANQUE)).thenReturn(List.of());

        List<String> consejos = servicio.evaluarTendencias(ESTANQUE);

        assertTrue(consejos.stream().anyMatch(c -> c.contains("recambio de agua")));
    }

    @Test
    void valoresEnMeseta_noGeneranConsejos() {
        stubHistorico(List.of(
            lectura(7, 14, 6.0, 0.01),
            lectura(7, 14, 6.0, 0.01),
            lectura(7, 14, 6.0, 0.01)));
        when(loteRepository.findByEstanqueId(ESTANQUE)).thenReturn(List.of());

        assertTrue(servicio.evaluarTendencias(ESTANQUE).isEmpty());
    }

    @Test
    void temperaturaBajandoConLoteActivo_recomiendaProtegerDelFrio() {
        // trucha: rango 10-18, centro 14; actual 11 (<14) y bajando de forma sostenida
        stubHistorico(List.of(
            lectura(7, 11, 8, 0.01),
            lectura(7, 13, 8, 0.01),
            lectura(7, 15, 8, 0.01)));
        Lote lote = new Lote();
        lote.setEspecie("trucha");
        lote.setEstado(LoteEstado.activo);
        when(loteRepository.findByEstanqueId(ESTANQUE)).thenReturn(List.of(lote));

        List<String> consejos = servicio.evaluarTendencias(ESTANQUE);

        assertTrue(consejos.stream().anyMatch(c -> c.contains("frío")));
    }

    @Test
    void especieDesconocida_noEvaluaLimites() {
        stubHistorico(List.of(
            lectura(7, 11, 8, 0.01),
            lectura(7, 13, 8, 0.01),
            lectura(7, 15, 8, 0.01)));
        Lote lote = new Lote();
        lote.setEspecie("carpa");
        lote.setEstado(LoteEstado.activo);
        when(loteRepository.findByEstanqueId(ESTANQUE)).thenReturn(List.of(lote));

        List<String> consejos = servicio.evaluarTendencias(ESTANQUE);

        assertFalse(consejos.stream().anyMatch(c -> c.contains("frío")));
    }
}
