package com.pezcasesor.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RegistroCrecimientoRegistroDTO {
    private BigDecimal pesoPromedioG;
    private BigDecimal tallaCm;
    private Integer mortalidad;
    private LocalDate fechaMuestreo;

    public BigDecimal getPesoPromedioG() { return pesoPromedioG; }
    public void setPesoPromedioG(BigDecimal pesoPromedioG) { this.pesoPromedioG = pesoPromedioG; }
    public BigDecimal getTallaCm() { return tallaCm; }
    public void setTallaCm(BigDecimal tallaCm) { this.tallaCm = tallaCm; }
    public Integer getMortalidad() { return mortalidad; }
    public void setMortalidad(Integer mortalidad) { this.mortalidad = mortalidad; }
    public LocalDate getFechaMuestreo() { return fechaMuestreo; }
    public void setFechaMuestreo(LocalDate fechaMuestreo) { this.fechaMuestreo = fechaMuestreo; }
}
