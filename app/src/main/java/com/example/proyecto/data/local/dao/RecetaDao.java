package com.example.proyecto.data.local.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.proyecto.data.cache.RecetaEntity; // Importa tu entidad

import java.util.List;

@Dao
public interface RecetaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertReceta(RecetaEntity receta);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllRecetas(List<RecetaEntity> recetas);

    @Update
    void updateReceta(RecetaEntity receta);

    @Delete
    void deleteReceta(RecetaEntity receta);

    @Query("DELETE FROM recetas")
    void deleteAllRecetas();

    @Query("SELECT * FROM recetas ORDER BY fecha_publicacion DESC")
    LiveData<List<RecetaEntity>> getAllRecetas();

    @Query("SELECT * FROM recetas WHERE id = :recetaId")
    LiveData<RecetaEntity> getRecetaById(int recetaId);

    @Query("SELECT * FROM recetas WHERE user_creator_username = :username ORDER BY fecha_publicacion DESC")
    LiveData<List<RecetaEntity>> getRecetasByUsername(String username);

}