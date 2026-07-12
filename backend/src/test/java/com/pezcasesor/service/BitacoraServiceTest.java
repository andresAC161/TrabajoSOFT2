package com.pezcasesor.service;

import com.pezcasesor.model.Bitacora;
import com.pezcasesor.model.BitacoraRespuestaDTO;
import com.pezcasesor.model.Usuario;
import com.pezcasesor.repository.BitacoraRepository;
import com.pezcasesor.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Pruebas unitarias de la bitacora e historial de actividades.
class BitacoraServiceTest {

    private BitacoraRepository bitacoraRepository;
    private UsuarioRepository usuarioRepository;
    private BitacoraService servicio;

    @BeforeEach
    void setUp() {
        bitacoraRepository = mock(BitacoraRepository.class);
        usuarioRepository = mock(UsuarioRepository.class);
        servicio = new BitacoraService(bitacoraRepository, usuarioRepository);
    }

    @Test
    void registrar_persisteLaAccionConFecha() {
        servicio.registrar(7L, "Registró parámetros de agua");

        ArgumentCaptor<Bitacora> captor = ArgumentCaptor.forClass(Bitacora.class);
        verify(bitacoraRepository).save(captor.capture());
        Bitacora guardada = captor.getValue();
        assertEquals(7L, guardada.getUsuarioId());
        assertEquals("Registró parámetros de agua", guardada.getAccion());
        assertNotNull(guardada.getFechaRegistro());
    }

    @Test
    void listar_resuelveNombreDeUsuario() {
        Bitacora b = new Bitacora();
        b.setId(1L);
        b.setUsuarioId(7L);
        b.setAccion("Programó una tarea");
        b.setFechaRegistro(LocalDateTime.now());
        when(bitacoraRepository.findAllByOrderByFechaRegistroDesc()).thenReturn(List.of(b));
        Usuario u = new Usuario();
        u.setNombre("Humberto");
        when(usuarioRepository.findById(7L)).thenReturn(Optional.of(u));

        List<BitacoraRespuestaDTO> res = servicio.listar();

        assertEquals(1, res.size());
        assertEquals("Humberto", res.get(0).getUsuarioNombre());
    }

    @Test
    void listar_usuarioInexistente_muestraEtiquetaPorDefecto() {
        Bitacora b = new Bitacora();
        b.setId(1L);
        b.setUsuarioId(99L);
        b.setAccion("Acción huérfana");
        b.setFechaRegistro(LocalDateTime.now());
        when(bitacoraRepository.findAllByOrderByFechaRegistroDesc()).thenReturn(List.of(b));
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        List<BitacoraRespuestaDTO> res = servicio.listar();

        assertEquals("Usuario eliminado", res.get(0).getUsuarioNombre());
    }
}
