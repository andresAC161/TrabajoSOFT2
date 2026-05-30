package com.pezcasesor.model;

import java.time.LocalDateTime;

public class TareaRespuestaDTO {
    private Long id;
    private Long usuarioId;
    private Long estanqueId;
    private Long loteId;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaHora;
    private String estado;
    private boolean notificado;

    public TareaRespuestaDTO(Long id, Long usuarioId, Long estanqueId, Long loteId,
                             String nombre, String descripcion, LocalDateTime fechaHora,
                             String estado, boolean notificado) {
        this.id = id; this.usuarioId = usuarioId; this.estanqueId = estanqueId;
        this.loteId = loteId; this.nombre = nombre; this.descripcion = descripcion;
        this.fechaHora = fechaHora; this.estado = estado; this.notificado = notificado;
    }

    public Long getId() { return id; }
    public Long getUsuarioId() { return usuarioId; }
    public Long getEstanqueId() { return estanqueId; }
    public Long getLoteId() { return loteId; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public String getEstado() { return estado; }
    public boolean isNotificado() { return notificado; }
}
