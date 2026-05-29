package com.pezcasesor.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "estanques")
public class Estanque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "estanque_id")
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "tipo_agua", nullable = false, length = 10)
    private String tipoAgua;

    @Column(name = "capacidad_litros", nullable = false, precision = 12, scale = 2)
    private BigDecimal capacidadLitros;

    @Column(length = 200)
    private String localizacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private EstanqueEstado estado;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTipoAgua() { return tipoAgua; }
    public void setTipoAgua(String tipoAgua) { this.tipoAgua = tipoAgua; }
    public BigDecimal getCapacidadLitros() { return capacidadLitros; }
    public void setCapacidadLitros(BigDecimal c) { this.capacidadLitros = c; }
    public String getLocalizacion() { return localizacion; }
    public void setLocalizacion(String localizacion) { this.localizacion = localizacion; }
    public EstanqueEstado getEstado() { return estado; }
    public void setEstado(EstanqueEstado estado) { this.estado = estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime f) { this.fechaCreacion = f; }
}
