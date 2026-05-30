package com.pezcasesor.model;

import java.math.BigDecimal;

public class ParametroAguaRegistroDTO {
    private Long estanqueId;
    private Long usuarioId;
    private BigDecimal ph;
    private BigDecimal temperaturaC;
    private BigDecimal oxigenoMgl;
    private BigDecimal amoniacoMgl;

    public Long getEstanqueId() { return estanqueId; }
    public void setEstanqueId(Long estanqueId) { this.estanqueId = estanqueId; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public BigDecimal getPh() { return ph; }
    public void setPh(BigDecimal ph) { this.ph = ph; }
    public BigDecimal getTemperaturaC() { return temperaturaC; }
    public void setTemperaturaC(BigDecimal t) { this.temperaturaC = t; }
    public BigDecimal getOxigenoMgl() { return oxigenoMgl; }
    public void setOxigenoMgl(BigDecimal o) { this.oxigenoMgl = o; }
    public BigDecimal getAmoniacoMgl() { return amoniacoMgl; }
    public void setAmoniacoMgl(BigDecimal a) { this.amoniacoMgl = a; }
}
