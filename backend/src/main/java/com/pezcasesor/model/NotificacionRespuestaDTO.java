package com.pezcasesor.model;

import java.time.LocalDateTime;

public class NotificacionRespuestaDTO {
    private Long id;
    private Long tareaId;
    private Long usuarioId;
    private String tipo;
    private String mensaje;
    private boolean leida;
    private LocalDateTime fechaEnvio;

    public NotificacionRespuestaDTO(Long id, Long tareaId, Long usuarioId,
                                    String tipo, String mensaje,
                                    boolean leida, LocalDateTime fechaEnvio) {
        this.id = id; this.tareaId = tareaId; this.usuarioId = usuarioId;
        this.tipo = tipo; this.mensaje = mensaje;
        this.leida = leida; this.fechaEnvio = fechaEnvio;
    }

    public Long getId() { return id; }
    public Long getTareaId() { return tareaId; }
    public Long getUsuarioId() { return usuarioId; }
    public String getTipo() { return tipo; }
    public String getMensaje() { return mensaje; }
    public boolean isLeida() { return leida; }
    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
}
