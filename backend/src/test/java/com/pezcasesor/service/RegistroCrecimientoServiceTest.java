package com.pezcasesor.service;

import com.pezcasesor.model.Lote;
import com.pezcasesor.model.LoteEstado;
import com.pezcasesor.model.RegistroCrecimiento;
import com.pezcasesor.repository.LoteRepository;
import com.pezcasesor.repository.RegistroCrecimientoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Pruebas unitarias del historial de crecimiento del lote.
class RegistroCrecimientoServiceTest {

    private RegistroCrecimientoRepository repository;
    private LoteRepository loteRepository;
    private RegistroCrecimientoService servicio;

    @BeforeEach
    void setUp() {
        repository = mock(RegistroCrecimientoRepository.class);
        loteRepository = mock(LoteRepository.class);
        servicio = new RegistroCrecimientoService(repository, loteRepository);

        Lote loteActivo = new Lote();
        loteActivo.setId(1L);
        loteActivo.setEstado(LoteEstado.activo);
        when(loteRepository.findById(1L)).thenReturn(Optional.of(loteActivo));
    }

    private RegistroCrecimiento registro(BigDecimal peso, LocalDate fecha) {
        RegistroCrecimiento r = new RegistroCrecimiento();
        r.setLoteId(1L);
        r.setPesoPromedioG(peso);
        r.setFechaMuestreo(fecha);
        return r;
    }

    @Test
    void pesoCero_lanzaExcepcion() {
        RegistroCrecimiento r = registro(BigDecimal.ZERO, LocalDate.now());
        assertThrows(IllegalArgumentException.class, () -> servicio.registrar(r));
        verify(repository, never()).save(any());
    }

    @Test
    void pesoNegativo_lanzaExcepcion() {
        RegistroCrecimiento r = registro(new BigDecimal("-5"), LocalDate.now());
        assertThrows(IllegalArgumentException.class, () -> servicio.registrar(r));
    }

    @Test
    void sinFecha_asignaFechaActual() {
        RegistroCrecimiento r = registro(new BigDecimal("120.5"), null);
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        RegistroCrecimiento guardado = servicio.registrar(r);

        assertEquals(LocalDate.now(), guardado.getFechaMuestreo());
        verify(repository).save(r);
    }

    @Test
    void registroValido_conservaFecha() {
        LocalDate fecha = LocalDate.of(2026, 6, 1);
        RegistroCrecimiento r = registro(new BigDecimal("120.5"), fecha);
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        RegistroCrecimiento guardado = servicio.registrar(r);

        assertEquals(fecha, guardado.getFechaMuestreo());
    }

    @Test
    void tallaNoPositiva_lanzaExcepcion() {
        RegistroCrecimiento r = registro(new BigDecimal("120.5"), LocalDate.now());
        r.setTallaCm(BigDecimal.ZERO);
        assertThrows(IllegalArgumentException.class, () -> servicio.registrar(r));
        verify(repository, never()).save(any());
    }

    @Test
    void mortalidadNegativa_lanzaExcepcion() {
        RegistroCrecimiento r = registro(new BigDecimal("120.5"), LocalDate.now());
        r.setMortalidad(-1);
        assertThrows(IllegalArgumentException.class, () -> servicio.registrar(r));
        verify(repository, never()).save(any());
    }

    @Test
    void registroConTallaYMortalidadValidas_persiste() {
        RegistroCrecimiento r = registro(new BigDecimal("120.5"), LocalDate.now());
        r.setTallaCm(new BigDecimal("18.3"));
        r.setMortalidad(4);
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        RegistroCrecimiento guardado = servicio.registrar(r);

        assertEquals(new BigDecimal("18.3"), guardado.getTallaCm());
        assertEquals(4, guardado.getMortalidad());
        verify(repository).save(r);
    }

    @Test
    void loteFinalizado_lanzaIllegalStateYNoPersiste() {
        Lote loteFinalizado = new Lote();
        loteFinalizado.setId(1L);
        loteFinalizado.setEstado(LoteEstado.finalizado);
        when(loteRepository.findById(1L)).thenReturn(Optional.of(loteFinalizado));

        RegistroCrecimiento r = registro(new BigDecimal("120.5"), LocalDate.now());
        assertThrows(IllegalStateException.class, () -> servicio.registrar(r));
        verify(repository, never()).save(any());
    }

    @Test
    void loteNoEncontrado_lanzaIllegalArgument() {
        when(loteRepository.findById(1L)).thenReturn(Optional.empty());

        RegistroCrecimiento r = registro(new BigDecimal("120.5"), LocalDate.now());
        assertThrows(IllegalArgumentException.class, () -> servicio.registrar(r));
        verify(repository, never()).save(any());
    }

    @Test
    void listarPorLote_delegaEnRepositorioOrdenado() {
        when(repository.findByLoteIdOrderByFechaMuestreoAsc(1L))
            .thenReturn(List.of(registro(new BigDecimal("100"), LocalDate.now())));

        assertEquals(1, servicio.listarPorLote(1L).size());
        verify(repository).findByLoteIdOrderByFechaMuestreoAsc(1L);
    }
}
