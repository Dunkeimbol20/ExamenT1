package com.example.proyecto.data.local.dao;

// En UsuarioDao.java



import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.proyecto.data.local.model.Usuario;

import java.util.List;

@Dao
public interface UsuarioDao {


    @Insert
    void guardarUsuarioParaFallback(Usuario usuario);

    @Query("SELECT * FROM usuarios WHERE username = :username LIMIT 1")
    Usuario obtenerUsuarioParaFallbackPorUsername(String username);

    @Query("DELETE FROM usuarios WHERE username = :username")
    void eliminarUsuarioFallbackPorUsername(String username);

    @Query("SELECT * FROM usuarios")
    List<Usuario> obtenerTodosLosUsuariosFallback(); // Nombre ligeramente ajustado
}