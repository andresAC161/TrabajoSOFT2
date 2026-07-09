package com.pezcasesor.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ParametroAguaRespuestaDTO {
    private Long id;
    private Long estanqueId;
    private Long usuarioId;
    private BigDecimal ph;
    private BigDecimal temperaturaC;
    private BigDecimal oxigenoMgl;
    private BigDecimal amoniacoMgl;
    private LocalDateTime fechaRegistro;
    private List<String> alertas;
    private List<String> consejos;

    public ParametroAguaRespuestaDTO(Long id, Long estanqueId, Long usuarioId,
                                     BigDecimal ph, BigDecimal temperaturaC,
                                     BigDecimal oxigenoMgl, BigDecimal amoniacoMgl,
                                     LocalDateTime fechaRegistro, List<String> alertas,
                                     List<String> consejos) {
        this.id = id; this.estanqueId = estanqueId; this.usuarioId = usuarioId;
        this.ph = ph; this.temperaturaC = temperaturaC; this.oxigenoMgl = oxigenoMgl;
        this.amoniacoMgl = amoniacoMgl; this.fechaRegistro = fechaRegistro;
        this.alertas = alertas;
        this.consejos = consejos;
    }

    public Long getId() { return id; }
    public Long getEstanqueId() { return estanqueId; }
    public Long getUsuarioId() { return usuarioId; }
    public BigDecimal getPh() { return ph; }
    public BigDecimal getTemperaturaC() { return temperaturaC; }
    public BigDecimal getOxigenoMgl() { return oxigenoMgl; }
    public BigDecimal getAmoniacoMgl() { return amoniacoMgl; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public List<String> getAlertas() { return alertas; }
    public List<String> getConsejos() { return consejos; }
}
