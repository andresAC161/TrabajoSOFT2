package com.pezcasesor.model;

public class GuiaRespuestaDTO {
    private Long id;
    private String especie;
    private String categoria;
    private String parametro;
    private String titulo;
    private String contenido;

    public GuiaRespuestaDTO(Long id, String especie, String categoria, String parametro,
                             String titulo, String contenido) {
        this.id = id;
        this.especie = especie;
        this.categoria = categoria;
        this.parametro = parametro;
        this.titulo = titulo;
        this.contenido = contenido;
    }

    public Long getId() { return id; }
    public String getEspecie() { return especie; }
    public String getCategoria() { return categoria; }
    public String getParametro() { return parametro; }
    public String getTitulo() { return titulo; }
    public String getContenido() { return contenido; }
}
