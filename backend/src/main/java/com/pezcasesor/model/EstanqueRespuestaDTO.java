package com.pezcasesor.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EstanqueRespuestaDTO {
    private Long id;
    private Long usuarioId;
    private String nombre;
    private String tipoAgua;
    private BigDecimal capacidadLitros;
    private String localizacion;
    private String estado;
    private LocalDateTime fechaCreacion;
    private boolean alertaActiva;

    public EstanqueRespuestaDTO(Long id, Long usuarioId, String nombre, String tipoAgua,
                                BigDecimal capacidadLitros, String localizacion,
                                String estado, LocalDateTime fechaCreacion, boolean alertaActiva) {
        this.id = id; this.usuarioId = usuarioId; this.nombre = nombre;
        this.tipoAgua = tipoAgua; this.capacidadLitros = capacidadLitros;
        this.localizacion = localizacion; this.estado = estado; this.fechaCreacion = fechaCreacion;
        this.alertaActiva = alertaActiva;
    }

    public Long getId() { return id; }
    public Long getUsuarioId() { return usuarioId; }
    public String getNombre() { return nombre; }
    public String getTipoAgua() { return tipoAgua; }
    public BigDecimal getCapacidadLitros() { return capacidadLitros; }
    public String getLocalizacion() { return localizacion; }
    public String getEstado() { return estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public boolean isAlertaActiva() { return alertaActiva; }
}
