package com.example.proyecto.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.example.proyecto.login_user.login_user;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;

public class SessionManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "MiAppProyectoPref"; // Nombre de tu archivo de preferencias
    private static final String IS_LOGIN = "IsLoggedIn";

    // --- CLAVES PARA DATOS DEL USUARIO ---
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_USER_NOMBRE = "userNombre"; // O KEY_USER_USERNAME, como prefieras
    // public static final String KEY_USER_EMAIL = "userEmail"; // <-- ELIMINA O COMENTA ESTA LÍNEA

    // --- CLAVE PARA EL TOKEN DE AUTENTICACIÓN ---
    public static final String KEY_AUTH_TOKEN = "authToken";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void saveAuthToken(String token) {
        editor.putBoolean(IS_LOGIN, true); // Marcar que el usuario está logueado
        editor.putString(KEY_AUTH_TOKEN, token);
        editor.commit(); // O apply()
    }


    public String getAuthToken() {
        return pref.getString(KEY_AUTH_TOKEN, null);
    }

    public void saveUserProfile(int userId, String nombre) { // <-- MODIFICADO: sin email
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USER_NOMBRE, nombre);
        // editor.putString(KEY_USER_EMAIL, email); // <-- ELIMINA O COMENTA ESTA LÍNEA
        editor.commit(); // O apply()
    }
    public HashMap<String, String> getUserProfile() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_USER_ID, String.valueOf(pref.getInt(KEY_USER_ID, 0))); // 0 como default para ID
        user.put(KEY_USER_NOMBRE, pref.getString(KEY_USER_NOMBRE, null));
        // user.put(KEY_USER_EMAIL, pref.getString(KEY_USER_EMAIL, null)); // <-- ELIMINA O COMENTA ESTA LÍNEA
        return user;
    }

    public String getUserNombre() { // O getUserUsername()
        return pref.getString(KEY_USER_NOMBRE, null);
    }



    public int getUserId() {
        return pref.getInt(KEY_USER_ID, 0); // 0 o -1 como valor por defecto si no se encuentra
    }

    public void logoutUser() {
        // Limpiar todos los datos de SharedPreferences
        editor.clear();
        editor.commit();

        // Después de logout redirigir al Login Activity
        Intent i = new Intent(_context, login_user.class); // Asegúrate que login_user.class sea tu actividad de login
        // Añadir flags para limpiar el stack de actividades
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}
