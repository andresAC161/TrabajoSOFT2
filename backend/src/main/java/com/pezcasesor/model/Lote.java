package com.pezcasesor.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "lotes")
public class Lote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lote_id")
    private Long id;

    @Column(name = "estanque_id", nullable = false)
    private Long estanqueId;

    @Column(nullable = false, length = 100)
    private String especie;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "fecha_siembra", nullable = false)
    private LocalDate fechaSiembra;

    @Column(name = "peso_inicial_g", nullable = false, precision = 8, scale = 2)
    private BigDecimal pesoInicialG;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private LoteEstado estado;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public LoteEstado getEstado() { return estado; }
    public void setEstado(LoteEstado estado) { this.estado = estado; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
}
