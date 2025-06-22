package com.example.proyecto.data.local.model;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "usuarios") // Nombre de la tabla en Room
public class Usuario {

    @PrimaryKey(autoGenerate = true)
    public int idLocal;

    @NonNull
    public String username;

    public String nombre;
    public String apellidos;
    public String fechaNacimiento;
    public String fechaRegistro;
    public String imagenPerfil;

    public Usuario(@NonNull String username, String nombre, String apellidos,
                   String fechaNacimiento, String fechaRegistro, String imagenPerfil) {
        this.username = username;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaRegistro = fechaRegistro;
        this.imagenPerfil = imagenPerfil;
    }

    // Constructor vacío que Room a veces necesita.
    public Usuario() {}
}