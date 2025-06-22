package com.example.proyecto.bd;

import java.util.Date;

public class Receta {
    private int id;
    private String titulo;
    private Date fechaPublicacion;
    private String imagenReceta;
    private String dificultad;
    private String estacion;
    private String tiempo;
    private String estadoEconomico;
    private String ingredientes;
    private String preparacion;
    private Usuario usuario;  // Relación ManyToOne

    // Getters y Setters

    public int getId() {
        return id;
    }
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getImagenReceta() {
        return imagenReceta;
    }

    public void setImagenReceta(String imagenReceta) {
        this.imagenReceta = imagenReceta;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public String getEstacion() {
        return estacion;
    }

    public void setEstacion(String estacion) {
        this.estacion = estacion;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getEstadoEconomico() {
        return estadoEconomico;
    }

    public void setEstadoEconomico(String estadoEconomico) {
        this.estadoEconomico = estadoEconomico;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getPreparacion() {
        return preparacion;
    }

    public void setPreparacion(String preparacion) {
        this.preparacion = preparacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
