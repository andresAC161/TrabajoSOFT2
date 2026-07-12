package com.pezcasesor.service;

import com.pezcasesor.model.Nota;
import com.pezcasesor.repository.NotaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Pruebas unitarias del registro de notas de un lote (HU14).
class NotaServiceTest {

    private NotaRepository notaRepository;
    private NotaService servicio;

    @BeforeEach
    void setUp() {
        notaRepository = mock(NotaRepository.class);
        servicio = new NotaService(notaRepository);
    }

    @Test
    void agregarNotaValida_persisteConFechaYLote() {
        when(notaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Nota nota = servicio.agregar(5L, "Se observó buen apetito del lote");

        ArgumentCaptor<Nota> captor = ArgumentCaptor.forClass(Nota.class);
        verify(notaRepository).save(captor.capture());
        Nota guardada = captor.getValue();
        assertEquals(5L, guardada.getLoteId());
        assertEquals("Se observó buen apetito del lote", guardada.getContenido());
        assertNotNull(guardada.getFechaHora());
        assertNotNull(nota);
    }

    @Test
    void agregarContenidoNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> servicio.agregar(5L, null));
        verify(notaRepository, never()).save(any());
    }

    @Test
    void agregarContenidoVacio_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> servicio.agregar(5L, "   "));
        verify(notaRepository, never()).save(any());
    }

    @Test
    void agregarNota_recortaEspacios() {
        when(notaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Nota nota = servicio.agregar(5L, "   nota con espacios   ");

        assertEquals("nota con espacios", nota.getContenido());
    }

    @Test
    void listarPorLote_delegaEnRepositorio() {
        Nota n = new Nota();
        n.setLoteId(5L);
        when(notaRepository.findByLoteId(5L)).thenReturn(List.of(n));

        assertEquals(1, servicio.listarPorLote(5L).size());
        verify(notaRepository).findByLoteId(5L);
    }
}
