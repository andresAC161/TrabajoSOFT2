package com.pezcasesor.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RegistroCrecimientoRespuestaDTO {
    private Long id;
    private Long loteId;
    private BigDecimal pesoPromedioG;
    private BigDecimal tallaCm;
    private Integer mortalidad;
    private LocalDate fechaMuestreo;

    public RegistroCrecimientoRespuestaDTO(Long id, Long loteId, BigDecimal pesoPromedioG,
                                            BigDecimal tallaCm, Integer mortalidad,
                                            LocalDate fechaMuestreo) {
        this.id = id;
        this.loteId = loteId;
        this.pesoPromedioG = pesoPromedioG;
        this.tallaCm = tallaCm;
        this.mortalidad = mortalidad;
        this.fechaMuestreo = fechaMuestreo;
    }

    public Long getId() { return id; }
    public Long getLoteId() { return loteId; }
    public BigDecimal getPesoPromedioG() { return pesoPromedioG; }
    public BigDecimal getTallaCm() { return tallaCm; }
    public Integer getMortalidad() { return mortalidad; }
    public LocalDate getFechaMuestreo() { return fechaMuestreo; }
}
