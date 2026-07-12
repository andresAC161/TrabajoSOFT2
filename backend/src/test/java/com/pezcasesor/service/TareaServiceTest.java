package com.pezcasesor.service;

import com.pezcasesor.model.Tarea;
import com.pezcasesor.model.TareaEditarDTO;
import com.pezcasesor.model.TareaEstado;
import com.pezcasesor.model.TareaRegistroDTO;
import com.pezcasesor.model.TareaRespuestaDTO;
import com.pezcasesor.repository.TareaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

// Pruebas unitarias de la gestion y filtrado de tareas por estado (HU15).
class TareaServiceTest {

    private TareaRepository tareaRepository;
    private BitacoraService bitacoraService;
    private TareaService servicio;

    @BeforeEach
    void setUp() {
        tareaRepository = mock(TareaRepository.class);
        bitacoraService = mock(BitacoraService.class);
        servicio = new TareaService(tareaRepository, bitacoraService);
    }

    private TareaRegistroDTO dtoValido() {
        TareaRegistroDTO dto = new TareaRegistroDTO();
        dto.setUsuarioId(1L);
        dto.setNombre("Alimentar peces");
        dto.setFechaHora(LocalDateTime.of(2026, 7, 20, 8, 0));
        return dto;
    }

    @Test
    void crearTareaValida_quedaPendienteYRegistraBitacora() {
        when(tareaRepository.save(any())).thenAnswer(inv -> {
            Tarea t = inv.getArgument(0);
            t.setId(5L);
            return t;
        });

        TareaRespuestaDTO res = servicio.crear(dtoValido());

        assertEquals(TareaEstado.pendiente.name(), res.getEstado());
        assertFalse(res.isNotificado());
        verify(bitacoraService).registrar(eq(1L), anyString());
    }

    @Test
    void crearSinNombre_lanzaExcepcion() {
        TareaRegistroDTO dto = dtoValido();
        dto.setNombre("   ");
        assertThrows(IllegalArgumentException.class, () -> servicio.crear(dto));
        verify(tareaRepository, never()).save(any());
    }

    @Test
    void crearSinFechaHora_lanzaExcepcion() {
        TareaRegistroDTO dto = dtoValido();
        dto.setFechaHora(null);
        assertThrows(IllegalArgumentException.class, () -> servicio.crear(dto));
    }

    @Test
    void actualizarACompletada_transicionaElEstado() {
        Tarea tarea = new Tarea();
        tarea.setId(5L);
        tarea.setNombre("Alimentar peces");
        tarea.setFechaHora(LocalDateTime.of(2026, 7, 20, 8, 0));
        tarea.setEstado(TareaEstado.pendiente);
        when(tareaRepository.findById(5L)).thenReturn(Optional.of(tarea));
        when(tareaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        TareaEditarDTO dto = new TareaEditarDTO();
        dto.setEstado("completada");

        TareaRespuestaDTO res = servicio.actualizar(5L, dto);

        assertEquals(TareaEstado.completada.name(), res.getEstado());
    }

    @Test
    void actualizarConEstadoInvalido_lanzaExcepcion() {
        Tarea tarea = new Tarea();
        tarea.setId(5L);
        tarea.setEstado(TareaEstado.pendiente);
        when(tareaRepository.findById(5L)).thenReturn(Optional.of(tarea));

        TareaEditarDTO dto = new TareaEditarDTO();
        dto.setEstado("archivada");

        assertThrows(IllegalArgumentException.class, () -> servicio.actualizar(5L, dto));
    }

    @Test
    void listarPorUsuarioYEstadoInvalido_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
            () -> servicio.listarPorUsuarioYEstado(1L, "inexistente"));
    }
}
