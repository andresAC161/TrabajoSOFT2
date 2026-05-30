package com.pezcasesor.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LoteRegistroDTO {
    private Long estanqueId;
    private String especie;
    private Integer cantidad;
    private LocalDate fechaSiembra;
    private BigDecimal pesoInicialG;

    public Long getEstanqueId() { return estanqueId; }
    public void setEstanqueId(Long estanqueId) { this.estanqueId = estanqueId; }
    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public LocalDate getFechaSiembra() { return fechaSiembra; }
    public void setFechaSiembra(LocalDate fechaSiembra) { this.fechaSiembra = fechaSiembra; }
    public BigDecimal getPesoInicialG() { return pesoInicialG; }
    public void setPesoInicialG(BigDecimal p) { this.pesoInicialG = p; }
}
