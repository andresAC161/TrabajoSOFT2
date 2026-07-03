package com.pezcasesor.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bitacora")
public class Bitacora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bitacora_id")
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(nullable = false, length = 200)
    private String accion;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime f) { this.fechaRegistro = f; }
}
