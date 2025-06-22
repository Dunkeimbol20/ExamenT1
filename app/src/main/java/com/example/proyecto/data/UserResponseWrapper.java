package com.example.proyecto.data;


import com.google.gson.annotations.SerializedName;

public class UserResponseWrapper { // Renombrada para mayor claridad, o puedes mantener UserResponse si la API siempre devuelve este wrapper

    @SerializedName("user")
    private UserData user;

    public UserData getUser() {
        return user;
    }
    public static class UserData {
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
        private String fechaNacimiento;

        @SerializedName("fecha_registro")
        private String fechaRegistro;

        // --- Constructores, Getters y Setters para UserData ---
        public UserData() {}

        // Getters
        public int getId() { return id; }
        public String getNombre() { return nombre; }
        public String getApellido() { return apellido; }
        public String getUsername() { return username; }
        public String getImagenPerfilUrl() { return imagenPerfilUrl; }
        public String getFechaNacimiento() { return fechaNacimiento; }
        public String getFechaRegistro() { return fechaRegistro; }

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

    @Override
    public String toString() {
        return "UserResponseWrapper{" +
                // "status='" + status + '\'' +
                ", user=" + (user != null ? user.toString() : "null") +
                '}';
    }
}