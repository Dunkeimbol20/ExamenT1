package com.example.proyecto.data.local.model;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "usuarios") // Nombre de la tabla en Room
public class Usuario {

    @PrimaryKey(autoGenerate = true)
    public int idLocal; // ID interno para Room, se genera solo

    @NonNull // Hacemos que el username no pueda ser null
    public String username; // Este es el que usaremos para buscar

    public String nombre;
    public String apellidos;
    public String fechaNacimiento;
    public String fechaRegistro;     // La fecha que vino del servidor
    public String imagenPerfil;      // La URL de la imagen que vino del servidor

    // ¡IMPORTANTE! No hay campo para 'contrasena' aquí.

    // Constructor que usarás para crear un objeto Usuario DESPUÉS de un login/registro exitoso con el servidor.
    // No ponemos 'idLocal' en el constructor porque Room lo genera.
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