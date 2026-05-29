package com.pezcasesor.model;

import java.time.LocalDateTime;

public class UsuarioRespuestaDTO {
    private Long id;
    private String nombre;
    private String correo;
    private String rol;
    private String ubicacion;
    private LocalDateTime fechaRegistro;

    public UsuarioRespuestaDTO(Long id, String nombre, String correo,
                               String rol, String ubicacion, LocalDateTime fechaRegistro) {
        this.id = id; this.nombre = nombre; this.correo = correo;
        this.rol = rol; this.ubicacion = ubicacion; this.fechaRegistro = fechaRegistro;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getRol() { return rol; }
    public String getUbicacion() { return ubicacion; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
}
