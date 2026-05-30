package com.pezcasesor.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ParametroAguaRespuestaDTO {
    private Long id;
    private Long estanqueId;
    private Long usuarioId;
    private BigDecimal ph;
    private BigDecimal temperaturaC;
    private BigDecimal oxigenoMgl;
    private BigDecimal amoniacoMgl;
    private LocalDateTime fechaRegistro;

    public ParametroAguaRespuestaDTO(Long id, Long estanqueId, Long usuarioId,
                                     BigDecimal ph, BigDecimal temperaturaC,
                                     BigDecimal oxigenoMgl, BigDecimal amoniacoMgl,
                                     LocalDateTime fechaRegistro) {
        this.id = id; this.estanqueId = estanqueId; this.usuarioId = usuarioId;
        this.ph = ph; this.temperaturaC = temperaturaC; this.oxigenoMgl = oxigenoMgl;
        this.amoniacoMgl = amoniacoMgl; this.fechaRegistro = fechaRegistro;
    }

    public Long getId() { return id; }
    public Long getEstanqueId() { return estanqueId; }
    public Long getUsuarioId() { return usuarioId; }
    public BigDecimal getPh() { return ph; }
    public BigDecimal getTemperaturaC() { return temperaturaC; }
    public BigDecimal getOxigenoMgl() { return oxigenoMgl; }
    public BigDecimal getAmoniacoMgl() { return amoniacoMgl; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
}
