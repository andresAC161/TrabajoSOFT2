package com.pezcasesor.model;

import java.time.LocalDateTime;

public class TareaRegistroDTO {
    private Long usuarioId;
    private Long estanqueId;
    private Long loteId;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaHora;

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public Long getEstanqueId() { return estanqueId; }
    public void setEstanqueId(Long estanqueId) { this.estanqueId = estanqueId; }
    public Long getLoteId() { return loteId; }
    public void setLoteId(Long loteId) { this.loteId = loteId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
}
