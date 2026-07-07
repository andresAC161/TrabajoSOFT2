package com.pezcasesor.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alertas")
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alerta_id")
    private Long id;

    @Column(name = "estanque_id", nullable = false)
    private Long estanqueId;

    @Column(name = "parametro_id")
    private Long parametroId;

    @Column(nullable = false, length = 200)
    private String mensaje;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEstanqueId() { return estanqueId; }
    public void setEstanqueId(Long estanqueId) { this.estanqueId = estanqueId; }
    public Long getParametroId() { return parametroId; }
    public void setParametroId(Long parametroId) { this.parametroId = parametroId; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}
