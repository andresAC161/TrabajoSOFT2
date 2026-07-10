package com.pezcasesor.model;

import java.time.LocalDateTime;

public class BitacoraRespuestaDTO {
    private Long id;
    private Long usuarioId;
    private String usuarioNombre;
    private String accion;
    private LocalDateTime fechaRegistro;

    public BitacoraRespuestaDTO(Long id, Long usuarioId, String usuarioNombre,
                                 String accion, LocalDateTime fechaRegistro) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.usuarioNombre = usuarioNombre;
        this.accion = accion;
        this.fechaRegistro = fechaRegistro;
    }

    public Long getId() { return id; }
    public Long getUsuarioId() { return usuarioId; }
    public String getUsuarioNombre() { return usuarioNombre; }
    public String getAccion() { return accion; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
}
