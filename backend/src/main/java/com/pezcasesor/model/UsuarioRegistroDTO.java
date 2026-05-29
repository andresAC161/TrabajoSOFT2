package com.pezcasesor.model;

public class UsuarioRegistroDTO {
    private String nombre;
    private String correo;
    private String contrasena;
    private String rol;
    private String ubicacion;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
}
