package com.pezcasesor.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 150)
    private String correo;

    @Column(name = "contrasena_hash", nullable = false)
    private String contrasenaHash;

    @Column(nullable = false, length = 20)
    private String rol;

    @Column(length = 200)
    private String ubicacion;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getContrasenaHash() { return contrasenaHash; }
    public void setContrasenaHash(String h) { this.contrasenaHash = h; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime f) { this.fechaRegistro = f; }
}
