package com.pezcasesor.model;

import java.math.BigDecimal;

public class EstanqueRegistroDTO {
    private Long usuarioId;
    private String nombre;
    private String tipoAgua;
    private BigDecimal capacidadLitros;
    private String localizacion;

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
}
