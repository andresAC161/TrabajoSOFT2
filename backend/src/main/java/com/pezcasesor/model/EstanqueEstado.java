package com.pezcasesor.model;

public enum EstanqueEstado {
    disponible, ocupado;

    public EstanqueEstado ocupar() {
        if (this == ocupado) throw new IllegalStateException("El estanque ya está ocupado.");
        return ocupado;
    }

    public EstanqueEstado liberar() {
        if (this == disponible) throw new IllegalStateException("El estanque ya está disponible.");
        return disponible;
    }
}
