package com.example.proyecto.data;

import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("user")
    private UserData user;



    // --- Constructores, Getters y Setters ---
    public UserResponse() {
    }

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }

    public String toString() { // Útil para debugging
        return "UserResponse{" +
                "user=" + (user != null ? user.toString() : "null") +
                // (status != null ? ", status='" + status + '\'' : "") +
                // (message != null ? ", message='" + message + '\'' : "") +
                '}';
    }

}