package com.pezcasesor.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LoteRespuestaDTO {
    private Long id;
    private Long estanqueId;
    private String especie;
    private Integer cantidad;
    private LocalDate fechaSiembra;
    private BigDecimal pesoInicialG;
    private String estado;
    private LocalDate fechaFin;

    public LoteRespuestaDTO(Long id, Long estanqueId, String especie, Integer cantidad,
                            LocalDate fechaSiembra, BigDecimal pesoInicialG,
                            String estado, LocalDate fechaFin) {
        this.id = id; this.estanqueId = estanqueId; this.especie = especie;
        this.cantidad = cantidad; this.fechaSiembra = fechaSiembra;
        this.pesoInicialG = pesoInicialG; this.estado = estado; this.fechaFin = fechaFin;
    }

    public Long getId() { return id; }
    public Long getEstanqueId() { return estanqueId; }
    public String getEspecie() { return especie; }
    public Integer getCantidad() { return cantidad; }
    public LocalDate getFechaSiembra() { return fechaSiembra; }
    public BigDecimal getPesoInicialG() { return pesoInicialG; }
    public String getEstado() { return estado; }
    public LocalDate getFechaFin() { return fechaFin; }
}
