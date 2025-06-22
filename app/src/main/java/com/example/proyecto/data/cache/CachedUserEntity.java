package com.example.proyecto.data.cache;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters; // Para Date
import java.util.Date;

@Entity(tableName = "cached_user_profile")
@TypeConverters(DateConverter.class)
public class CachedUserEntity {
    @ColumnInfo(name = "server_id")
    public int serverId;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "username")
    public String username;

    @ColumnInfo(name = "nombre")
    public String nombre;

    @ColumnInfo(name = "apellido")
    public String apellido;

    @ColumnInfo(name = "imagen_perfil_url")
    public String imagenPerfilUrl;

    @ColumnInfo(name = "fecha_nacimiento")
    public Date fechaNacimiento;

    @ColumnInfo(name = "fecha_registro")
    public Date fechaRegistro;
    @ColumnInfo(name = "contrasena_o_token")
    public String contrasenaOToken;


    @ColumnInfo(name = "fecha_cacheado_timestamp")
    public long fechaCacheadoTimestamp;

    public CachedUserEntity() {}

    public CachedUserEntity(int serverId, @NonNull String username, String nombre, String apellido,
                            String imagenPerfilUrl, Date fechaNacimiento, Date fechaRegistro,
                            String contrasenaOToken, long fechaCacheadoTimestamp) {
        this.serverId = serverId;
        this.username = username;
        this.nombre = nombre;
        this.apellido = apellido;
        this.imagenPerfilUrl = imagenPerfilUrl;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaRegistro = fechaRegistro;
        this.contrasenaOToken = contrasenaOToken;
        this.fechaCacheadoTimestamp = fechaCacheadoTimestamp;
    }


    public int getServerId() { return serverId; }
    public void setServerId(int serverId) { this.serverId = serverId; }

    @NonNull
    public String getUsername() { return username; }
    public void setUsername(@NonNull String username) { this.username = username; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getImagenPerfilUrl() { return imagenPerfilUrl; }
    public void setImagenPerfilUrl(String imagenPerfilUrl) { this.imagenPerfilUrl = imagenPerfilUrl; }

    public Date getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(Date fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public Date getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public String getContrasenaOToken() { return contrasenaOToken; }
    public void setContrasenaOToken(String contrasenaOToken) { this.contrasenaOToken = contrasenaOToken; }

    public long getFechaCacheadoTimestamp() { return fechaCacheadoTimestamp; }
    public void setFechaCacheadoTimestamp(long fechaCacheadoTimestamp) { this.fechaCacheadoTimestamp = fechaCacheadoTimestamp; }
}