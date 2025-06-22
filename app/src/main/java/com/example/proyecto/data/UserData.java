package com.example.proyecto.data;

import com.google.gson.annotations.SerializedName;

public class UserData {
    @SerializedName("id")
    private int id;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("apellido")
    private String apellido;

    @SerializedName("username")
    private String username;

    @SerializedName("imagen_perfil_url")
    private String imagenPerfilUrl;

    @SerializedName("fecha_nacimiento")
    private String fechaNacimiento; // Mantener como String, se parseará después

    @SerializedName("fecha_registro")
    private String fechaRegistro;   // Mantener como String, se parseará después

    // Constructor vacío (Gson puede necesitarlo)
    public UserData() {}

    // Getters (necesarios para acceder a los datos)
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getUsername() { return username; }
    public String getImagenPerfilUrl() { return imagenPerfilUrl; }
    public String getFechaNacimiento() { return fechaNacimiento; }
    public String getFechaRegistro() { return fechaRegistro; }

    // Setters (opcionales, Gson generalmente no los necesita para deserializar)
    // ... (puedes añadirlos si los necesitas)

    @Override
    public String toString() {
        return "UserData{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", username='" + username + '\'' +
                ", imagenPerfilUrl='" + imagenPerfilUrl + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", fechaRegistro='" + fechaRegistro + '\'' +
                '}';
    }
}