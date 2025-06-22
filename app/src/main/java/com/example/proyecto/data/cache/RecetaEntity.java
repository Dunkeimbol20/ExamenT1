package com.example.proyecto.data.cache;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;
// ...
import java.util.Date;
@Entity(tableName = "recetas",
        foreignKeys = @ForeignKey(entity = CachedUserEntity.class,
                parentColumns = "username", // La PK de CachedUserEntity
                childColumns = "user_creator_username", // La FK en RecetaEntity
                onDelete = ForeignKey.CASCADE), // Define qué pasa si se borra el usuario
        indices = {@Index(value = "user_creator_username")}) // Indexar la FK para mejor rendimiento
public class RecetaEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "titulo")
    public String titulo;

    @ColumnInfo(name = "fecha_publicacion") // El valor se asignará en el constructor para nuevas instancias
    @TypeConverters(DateConverter.class)
    public Date fechaPublicacion;

    @ColumnInfo(name = "imagen_receta_url")
    public String imagenRecetaUrl;

    @ColumnInfo(name = "dificultad")
    public String dificultad; // Ejemplo: "Fácil", "Media", "Difícil"

    @ColumnInfo(name = "estacion")
    public String estacion;   // Ejemplo: "Verano", "Todas"

    @ColumnInfo(name = "tiempo_preparacion")
    public String tiempoPreparacion;

    @ColumnInfo(name = "costo_aproximado")
    public String costoAproximado;

    @ColumnInfo(name = "ingredientes")
    public String ingredientes;

    @ColumnInfo(name = "preparacion")
    public String preparacion;

    @ColumnInfo(name = "categorias")
    public String categorias;    // Ejemplo: "Postres, Vegano", separado por comas o JSON array String

    @ColumnInfo(name = "user_creator_username") // Clave foránea al username del usuario
    public String userCreatorUsername;

    // Constructor vacío (Room lo necesita)
    public RecetaEntity() {
    }

    // Constructor con parámetros (útil para crear instancias)
    // Nota: El 'id' es autogenerado, por lo que no suele estar en el constructor principal
    public RecetaEntity(String titulo, String imagenRecetaUrl,
                        String dificultad, String estacion, String tiempoPreparacion,
                        String costoAproximado, String ingredientes, String preparacion,
                        String categorias, String userCreatorUsername) {
        this.titulo = titulo;
        this.fechaPublicacion = new Date();
        this.imagenRecetaUrl = imagenRecetaUrl;
        this.dificultad = dificultad;
        this.estacion = estacion;
        this.tiempoPreparacion = tiempoPreparacion;
        this.costoAproximado = costoAproximado;
        this.ingredientes = ingredientes;
        this.preparacion = preparacion;
        this.categorias = categorias;
        this.userCreatorUsername = userCreatorUsername;
    }


    // --- Getters y Setters (Room puede acceder a campos públicos, pero es buena práctica tenerlos) ---
    // Puedes generarlos automáticamente con tu IDE (Alt + Insert en Android Studio -> Getters and Setters)

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getImagenRecetaUrl() {
        return imagenRecetaUrl;
    }

    public void setImagenRecetaUrl(String imagenRecetaUrl) {
        this.imagenRecetaUrl = imagenRecetaUrl;
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

    public String getTiempoPreparacion() {
        return tiempoPreparacion;
    }

    public void setTiempoPreparacion(String tiempoPreparacion) {
        this.tiempoPreparacion = tiempoPreparacion;
    }

    public String getCostoAproximado() {
        return costoAproximado;
    }

    public void setCostoAproximado(String costoAproximado) {
        this.costoAproximado = costoAproximado;
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

    public String getCategorias() {
        return categorias;
    }

    public void setCategorias(String categorias) {
        this.categorias = categorias;
    }

    public String getUserCreatorUsername() {
        return userCreatorUsername;
    }

    public void setUserCreatorUsername(String userCreatorUsername) {
        this.userCreatorUsername = userCreatorUsername;
    }
}