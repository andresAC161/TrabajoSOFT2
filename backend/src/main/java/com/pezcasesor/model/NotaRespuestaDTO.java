package com.pezcasesor.model;

import java.time.LocalDateTime;

public class NotaRespuestaDTO {
    private Long id;
    private Long loteId;
    private String contenido;
    private LocalDateTime fechaHora;

    public NotaRespuestaDTO(Long id, Long loteId, String contenido, LocalDateTime fechaHora) {
        this.id = id;
        this.loteId = loteId;
        this.contenido = contenido;
        this.fechaHora = fechaHora;
    }

    public Long getId() { return id; }
    public Long getLoteId() { return loteId; }
    public String getContenido() { return contenido; }
    public LocalDateTime getFechaHora() { return fechaHora; }
}
