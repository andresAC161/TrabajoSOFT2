package com.pezcasesor.service;

import com.pezcasesor.model.Guia;
import com.pezcasesor.model.GuiaRespuestaDTO;
import com.pezcasesor.model.Lote;
import com.pezcasesor.model.ParametroAgua;
import com.pezcasesor.repository.ConsultaGuiaRepository;
import com.pezcasesor.repository.GuiaRepository;
import com.pezcasesor.repository.LoteRepository;
import com.pezcasesor.repository.ParametroAguaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

// Pruebas unitarias de las guías / consejos prácticos de manejo (HU12).
// Cubre la recomendación por lote según haya o no parámetros anómalos.
class GuiaServiceTest {

    private GuiaRepository guiaRepository;
    private ConsultaGuiaRepository consultaGuiaRepository;
    private LoteRepository loteRepository;
    private ParametroAguaRepository parametroAguaRepository;
    private GuiaService servicio;

    @BeforeEach
    void setUp() {
        guiaRepository = mock(GuiaRepository.class);
        consultaGuiaRepository = mock(ConsultaGuiaRepository.class);
        loteRepository = mock(LoteRepository.class);
        parametroAguaRepository = mock(ParametroAguaRepository.class);
        servicio = new GuiaService(guiaRepository, consultaGuiaRepository,
            loteRepository, parametroAguaRepository);
    }

    private Lote loteTrucha() {
        Lote lote = new Lote();
        lote.setId(1L);
        lote.setEstanqueId(3L);
        lote.setEspecie("trucha");
        return lote;
    }

    private ParametroAgua parametro(String ph, String temp, String oxi, String amo) {
        ParametroAgua p = new ParametroAgua();
        p.setPh(new BigDecimal(ph));
        p.setTemperaturaC(new BigDecimal(temp));
        p.setOxigenoMgl(new BigDecimal(oxi));
        p.setAmoniacoMgl(new BigDecimal(amo));
        return p;
    }

    private Guia guia(String categoria, String parametro) {
        Guia g = new Guia();
        g.setId(10L);
        g.setEspecie("trucha");
        g.setCategoria(categoria);
        g.setParametro(parametro);
        g.setTitulo("Título de guía");
        g.setContenido("Contenido de la guía");
        return g;
    }

    @Test
    void recomendarPorLote_loteInexistente_lanzaExcepcion() {
        when(loteRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> servicio.recomendarPorLote(99L));
        verify(consultaGuiaRepository, never()).save(any());
    }

    @Test
    void recomendarPorLote_sinParametrosAnomalos_devuelveGuiasDeAlimentacion() {
        when(loteRepository.findById(1L)).thenReturn(Optional.of(loteTrucha()));
        // parámetros en rango para trucha -> sin anomalías
        when(parametroAguaRepository.findFirstByEstanqueIdOrderByFechaRegistroDesc(3L))
            .thenReturn(Optional.of(parametro("7.0", "14", "8", "0.01")));
        when(guiaRepository.findByEspecieIgnoreCaseAndCategoria("trucha", "alimentacion"))
            .thenReturn(List.of(guia("alimentacion", null)));

        List<GuiaRespuestaDTO> res = servicio.recomendarPorLote(1L);

        assertEquals(1, res.size());
        assertEquals("alimentacion", res.get(0).getCategoria());
        verify(guiaRepository).findByEspecieIgnoreCaseAndCategoria("trucha", "alimentacion");
        verify(guiaRepository, never())
            .findByEspecieIgnoreCaseAndCategoriaAndParametroIn(anyString(), anyString(), any());
        verify(consultaGuiaRepository, times(1)).save(any());
    }

    @Test
    void recomendarPorLote_conParametroAnomalo_devuelveGuiasDeSanidad() {
        when(loteRepository.findById(1L)).thenReturn(Optional.of(loteTrucha()));
        // pH 6.0 está por debajo del mínimo de la trucha (6.5) -> parámetro anómalo "ph"
        when(parametroAguaRepository.findFirstByEstanqueIdOrderByFechaRegistroDesc(3L))
            .thenReturn(Optional.of(parametro("6.0", "14", "8", "0.01")));
        when(guiaRepository.findByEspecieIgnoreCaseAndCategoriaAndParametroIn(
                eq("trucha"), eq("sanidad"), eq(List.of("ph"))))
            .thenReturn(List.of(guia("sanidad", "ph")));

        List<GuiaRespuestaDTO> res = servicio.recomendarPorLote(1L);

        assertEquals(1, res.size());
        assertEquals("sanidad", res.get(0).getCategoria());
        verify(guiaRepository).findByEspecieIgnoreCaseAndCategoriaAndParametroIn(
            "trucha", "sanidad", List.of("ph"));
        verify(consultaGuiaRepository, times(1)).save(any());
    }

    @Test
    void recomendarPorLote_sinLecturas_usaGuiasDeAlimentacion() {
        when(loteRepository.findById(1L)).thenReturn(Optional.of(loteTrucha()));
        when(parametroAguaRepository.findFirstByEstanqueIdOrderByFechaRegistroDesc(3L))
            .thenReturn(Optional.empty());
        when(guiaRepository.findByEspecieIgnoreCaseAndCategoria("trucha", "alimentacion"))
            .thenReturn(List.of(guia("alimentacion", null)));

        List<GuiaRespuestaDTO> res = servicio.recomendarPorLote(1L);

        assertEquals(1, res.size());
        verify(guiaRepository).findByEspecieIgnoreCaseAndCategoria("trucha", "alimentacion");
    }

    @Test
    void listarPorEspecie_delegaYMapeaADTO() {
        when(guiaRepository.findByEspecieIgnoreCase("trucha"))
            .thenReturn(List.of(guia("alimentacion", null)));

        List<GuiaRespuestaDTO> res = servicio.listarPorEspecie("trucha");

        assertEquals(1, res.size());
        assertEquals("Título de guía", res.get(0).getTitulo());
        verify(guiaRepository).findByEspecieIgnoreCase("trucha");
    }
}
