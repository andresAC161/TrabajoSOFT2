package com.pezcasesor.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "parametros_agua")
public class ParametroAgua {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parametro_id")
    private Long id;

    @Column(name = "estanque_id", nullable = false)
    private Long estanqueId;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal ph;

    @Column(name = "temperatura_c", nullable = false, precision = 5, scale = 2)
    private BigDecimal temperaturaC;

    @Column(name = "oxigeno_mgl", nullable = false, precision = 5, scale = 2)
    private BigDecimal oxigenoMgl;

    @Column(name = "amoniaco_mgl", nullable = false, precision = 5, scale = 3)
    private BigDecimal amoniacoMgl;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime f) { this.fechaRegistro = f; }
}
