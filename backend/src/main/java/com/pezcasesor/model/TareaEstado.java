package com.pezcasesor.model;

public enum TareaEstado {
    pendiente, completada, cancelada;

    public TareaEstado completar() {
        if (this != pendiente) throw new IllegalStateException("Solo se pueden completar tareas pendientes.");
        return completada;
    }

    public TareaEstado cancelar() {
        if (this == completada) throw new IllegalStateException("No se puede cancelar una tarea ya completada.");
        return cancelada;
    }
}
