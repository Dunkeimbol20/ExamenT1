package com.example.proyecto.data.remote.api;
// ApiService.java
import com.example.proyecto.data.UserResponse;
import com.example.proyecto.data.UserResponseWrapper;
// ApiService.java


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @FormUrlEncoded
    @POST("registrar_usuario.php")
    Call<UserResponse> registrarUsuario(
                                         @Field("nombre") String nombre,
                                         @Field("apellido") String apellido,
                                         @Field("username") String username,
                                         @Field("contrasena") String contrasena,
                                         @Field("fecha_nacimiento") String fechaNacimiento,
                                         @Field("imagen_perfil_url") String imagenPerfilUrl
    );
    @FormUrlEncoded
    @POST("login_usuario.php") // O la ruta correcta a tu script de login
    Call<UserResponse> iniciarSesion( // <--- Asegúrate que devuelve Call<UserResponse>
                                      @Field("username") String username,
                                      @Field("contrasena") String contrasena
    );
    @GET("listar_usuarios.php")
    Call<List<UserResponseWrapper>> listarTodosLosUsuarios();
}