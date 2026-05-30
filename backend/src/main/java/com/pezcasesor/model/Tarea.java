package com.pezcasesor.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tareas")
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tarea_id")
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "estanque_id")
    private Long estanqueId;

    @Column(name = "lote_id")
    private Long loteId;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private TareaEstado estado;

    @Column(nullable = false)
    private boolean notificado;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public Long getEstanqueId() { return estanqueId; }
    public void setEstanqueId(Long estanqueId) { this.estanqueId = estanqueId; }
    public Long getLoteId() { return loteId; }
    public void setLoteId(Long loteId) { this.loteId = loteId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public TareaEstado getEstado() { return estado; }
    public void setEstado(TareaEstado estado) { this.estado = estado; }
    public boolean isNotificado() { return notificado; }
    public void setNotificado(boolean notificado) { this.notificado = notificado; }
}
