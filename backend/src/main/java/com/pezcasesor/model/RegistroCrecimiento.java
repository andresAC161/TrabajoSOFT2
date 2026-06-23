package com.pezcasesor.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "registros_crecimiento")
public class RegistroCrecimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registro_id")
    private Long id;

    @Column(name = "lote_id", nullable = false)
    private Long loteId;

    @Column(name = "peso_promedio_g", nullable = false, precision = 8, scale = 2)
    private BigDecimal pesoPromedioG;

    @Column(name = "talla_cm", precision = 6, scale = 2)
    private BigDecimal tallaCm;

    @Column
    private Integer mortalidad;

    @Column(name = "fecha_muestreo", nullable = false)
    private LocalDate fechaMuestreo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getLoteId() { return loteId; }
    public void setLoteId(Long loteId) { this.loteId = loteId; }
    public BigDecimal getPesoPromedioG() { return pesoPromedioG; }
    public void setPesoPromedioG(BigDecimal p) { this.pesoPromedioG = p; }
    public BigDecimal getTallaCm() { return tallaCm; }
    public void setTallaCm(BigDecimal t) { this.tallaCm = t; }
    public Integer getMortalidad() { return mortalidad; }
    public void setMortalidad(Integer m) { this.mortalidad = m; }
    public LocalDate getFechaMuestreo() { return fechaMuestreo; }
    public void setFechaMuestreo(LocalDate f) { this.fechaMuestreo = f; }
}
