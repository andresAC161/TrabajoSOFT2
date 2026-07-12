package com.pezcasesor.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Pruebas de caja blanca de la maquina de estados de la tarea (HU15).
// Cubre todas las transiciones validas e invalidas.
class TareaEstadoTest {

    @Test
    void pendiente_completar_transicionaACompletada() {
        assertEquals(TareaEstado.completada, TareaEstado.pendiente.completar());
    }

    @Test
    void pendiente_cancelar_transicionaACancelada() {
        assertEquals(TareaEstado.cancelada, TareaEstado.pendiente.cancelar());
    }

    @Test
    void completada_completar_lanzaExcepcion() {
        assertThrows(IllegalStateException.class, () -> TareaEstado.completada.completar());
    }

    @Test
    void cancelada_completar_lanzaExcepcion() {
        assertThrows(IllegalStateException.class, () -> TareaEstado.cancelada.completar());
    }

    @Test
    void completada_cancelar_lanzaExcepcion() {
        assertThrows(IllegalStateException.class, () -> TareaEstado.completada.cancelar());
    }

    @Test
    void cancelada_cancelar_esIdempotente() {
        assertEquals(TareaEstado.cancelada, TareaEstado.cancelada.cancelar());
    }
}
