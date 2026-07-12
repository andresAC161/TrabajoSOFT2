package com.pezcasesor.service;

import com.pezcasesor.model.Lote;
import com.pezcasesor.model.LoteEstado;
import com.pezcasesor.model.ParametroAgua;
import com.pezcasesor.model.ParametroAguaRegistroDTO;
import com.pezcasesor.model.ParametroAguaRespuestaDTO;
import com.pezcasesor.repository.AlertaRepository;
import com.pezcasesor.repository.LoteRepository;
import com.pezcasesor.repository.ParametroAguaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

// Pruebas unitarias/integracion del registro de parametros de agua,
// su validacion de dominio y la deteccion de alertas por umbral.
class ParametroAguaServiceTest {

    private ParametroAguaRepository parametroAguaRepository;
    private LoteRepository loteRepository;
    private AlertaRepository alertaRepository;
    private ServicioRecomendaciones servicioRecomendaciones;
    private BitacoraService bitacoraService;
    private ParametroAguaService servicio;

    @BeforeEach
    void setUp() {
        parametroAguaRepository = mock(ParametroAguaRepository.class);
        loteRepository = mock(LoteRepository.class);
        alertaRepository = mock(AlertaRepository.class);
        servicioRecomendaciones = mock(ServicioRecomendaciones.class);
        bitacoraService = mock(BitacoraService.class);
        servicio = new ParametroAguaService(parametroAguaRepository, loteRepository,
            alertaRepository, servicioRecomendaciones, bitacoraService);
    }

    private ParametroAguaRegistroDTO dto(String ph, String temp, String oxi, String amo) {
        ParametroAguaRegistroDTO d = new ParametroAguaRegistroDTO();
        d.setEstanqueId(1L);
        d.setUsuarioId(2L);
        d.setPh(ph == null ? null : new BigDecimal(ph));
        d.setTemperaturaC(temp == null ? null : new BigDecimal(temp));
        d.setOxigenoMgl(oxi == null ? null : new BigDecimal(oxi));
        d.setAmoniacoMgl(amo == null ? null : new BigDecimal(amo));
        return d;
    }

    private void stubPersistenciaBasica() {
        when(parametroAguaRepository.save(any())).thenAnswer(inv -> {
            ParametroAgua p = inv.getArgument(0);
            p.setId(100L);
            return p;
        });
        when(servicioRecomendaciones.evaluarTendencias(anyLong())).thenReturn(List.of());
    }

    @Test
    void registroValido_sinLoteActivo_persisteYRegistraBitacora() {
        stubPersistenciaBasica();
        when(loteRepository.findByEstanqueId(1L)).thenReturn(List.of());

        ParametroAguaRespuestaDTO res = servicio.registrar(dto("7.0", "15", "8", "0.01"));

        assertNotNull(res.getId());
        assertTrue(res.getAlertas().isEmpty());
        verify(bitacoraService).registrar(eq(2L), anyString());
        verify(alertaRepository, never()).save(any());
    }

    @Test
    void phFueraDeDominio_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
            () -> servicio.registrar(dto("14.5", "15", "8", "0.01")));
        verify(parametroAguaRepository, never()).save(any());
    }

    @Test
    void temperaturaNoPositiva_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
            () -> servicio.registrar(dto("7.0", "0", "8", "0.01")));
    }

    @Test
    void oxigenoNoPositivo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
            () -> servicio.registrar(dto("7.0", "15", "0", "0.01")));
    }

    @Test
    void amoniacoNegativo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
            () -> servicio.registrar(dto("7.0", "15", "8", "-0.1")));
    }

    @Test
    void valorFueraDeRangoConLoteActivo_generaAlertaYLaPersiste() {
        stubPersistenciaBasica();
        Lote lote = new Lote();
        lote.setEspecie("trucha");
        lote.setEstado(LoteEstado.activo);
        when(loteRepository.findByEstanqueId(1L)).thenReturn(List.of(lote));

        // pH 6.0 esta por debajo del minimo de la trucha (6.5); el resto en rango
        ParametroAguaRespuestaDTO res = servicio.registrar(dto("6.0", "15", "8", "0.01"));

        assertTrue(res.getAlertas().stream().anyMatch(a -> a.contains("pH fuera de rango")));
        verify(alertaRepository, times(1)).save(any());
    }

    @Test
    void listarPorEstanque_devuelveListaSinAlertas() {
        ParametroAgua p = new ParametroAgua();
        p.setId(1L);
        p.setPh(new BigDecimal("7.0"));
        p.setTemperaturaC(new BigDecimal("15"));
        p.setOxigenoMgl(new BigDecimal("8"));
        p.setAmoniacoMgl(new BigDecimal("0.01"));
        when(parametroAguaRepository.findByEstanqueIdOrderByFechaRegistroDesc(1L))
            .thenReturn(List.of(p));

        List<ParametroAguaRespuestaDTO> res = servicio.listarPorEstanque(1L);

        assertEquals(1, res.size());
        assertTrue(res.get(0).getAlertas().isEmpty());
    }
}
