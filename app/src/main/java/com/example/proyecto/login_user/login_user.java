package com.example.proyecto.login_user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.UnstableApi;

import com.example.proyecto.R;
import com.example.proyecto.data.UserData;
import com.example.proyecto.data.UserResponse;
import com.example.proyecto.data.cache.AppDatabase;
import com.example.proyecto.data.cache.CachedUserDao;
import com.example.proyecto.data.cache.CachedUserEntity;
import com.example.proyecto.data.local.dao.UsuarioDao;
import com.example.proyecto.data.local.model.Usuario;
import com.example.proyecto.data.remote.api.ApiService;
import com.example.proyecto.data.remote.client.RetrofitClient;
import com.example.proyecto.principal.NavbarSide.navBarSideActivity;
import com.example.proyecto.register_user.RegisterUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class login_user extends AppCompatActivity {
    EditText etLoginUsername,etLoginPassword;
    Button botonRegisterUser,botonLoginUser;
    private UserResponse userResponse;
    private ApiService apiService;
    public static final String PREFS_NAME = "MiAppPrefs";
    public static final String KEY_LOGGED_IN_USERNAME = "logged_in_username";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_PROFILE_IMAGE_URL = "user_profile_image_url";
    public static final String KEY_IS_LOGGED_IN = "IS_LOGGED_IN";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        botonRegisterUser = findViewById(R.id.btnRegisterUser);
        botonLoginUser = findViewById(R.id.botonLoginUser);
        //Button btnEliminarBD = findViewById(R.id.btn_eliminar_bd);
        //Asignar eventos a los botones
        apiService = RetrofitClient.getApiService();

        //Datos de verificacion
        etLoginUsername = findViewById(R.id.LoginEditTextUsername);
        etLoginPassword = findViewById(R.id.LoginEditTextContrasena);

        //verificar
        botonLoginUser.setOnClickListener(v->iniciarSesion());
        botonRegisterUser.setOnClickListener(v->page_registro());

    }

    private void page_registro() {
        Intent intent = new Intent(this, RegisterUser.class);
        startActivity(intent);
    }

    private void iniciarSesion() {
        String username = etLoginUsername.getText().toString().trim();
        String contrasena = etLoginPassword.getText().toString().trim();

        if (username.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa usuario y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<UserResponse> call = apiService.iniciarSesion(username, contrasena);
        call.enqueue(new Callback<UserResponse>() {
            @OptIn(markerClass = UnstableApi.class)
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse userResponse = response.body(); // userResponse es el wrapper
                    UserData userData = userResponse.getUser();   // Obtienes el UserData anidado

                    if (userData != null && userData.getUsername() != null && !userData.getUsername().isEmpty()) {
                        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(KEY_LOGGED_IN_USERNAME, userData.getUsername());
                        String nombreCompleto = "";
                        if (userData.getNombre() != null && !userData.getNombre().isEmpty()) {
                            nombreCompleto += userData.getNombre();
                        }
                        if (userData.getApellido() != null && !userData.getApellido().isEmpty()) {
                            if (!nombreCompleto.isEmpty()) nombreCompleto += " ";
                            nombreCompleto += userData.getApellido();
                        }
                        editor.putString(KEY_USER_NAME, nombreCompleto.isEmpty() ? userData.getUsername() : nombreCompleto);

                        editor.putString(KEY_USER_PROFILE_IMAGE_URL, userData.getImagenPerfilUrl());
                        editor.putBoolean(KEY_IS_LOGGED_IN, true);
                        editor.apply();
                        guardarUsuarioEnRoom(userData);
                        Toast.makeText(login_user.this, "Login Exitoso!", Toast.LENGTH_SHORT).show();
                        // Navegar a la siguiente actividad
                        Intent iPrincipal = new Intent(login_user.this, navBarSideActivity.class);
                        iPrincipal.putExtra("USER_USERNAME", userData.getUsername());
                        iPrincipal.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(iPrincipal);
                        finish();

                    } else {
                        Toast.makeText(login_user.this, "Error en los datos recibidos del servidor.", Toast.LENGTH_LONG).show();
                        autenticarLocal(username, contrasena);
                    }
                } else {
                    String errorMsg = "Credenciales inválidas o error del servidor.";
                    if (response.code() == 401) {
                        errorMsg = "Usuario o contraseña incorrectos.";
                    }
                    Toast.makeText(login_user.this, errorMsg, Toast.LENGTH_LONG).show();
                    // Lógica de fallback si falla la autenticación online
                    autenticarLocal(username, contrasena);
                }
            }

            @OptIn(markerClass = UnstableApi.class)
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(login_user.this, "Error de conexión. Intentando autenticación local.", Toast.LENGTH_LONG).show();
                autenticarLocal(username, contrasena); // Fallback a la autenticación local
            }
        });
    }
            @OptIn(markerClass = UnstableApi.class)
            private void guardarUsuarioEnRoom(UserData userData) {
                if (userData.getUsername() == null || userData.getUsername().trim().isEmpty()) {
                    return;
                }

                Date fechaNacimientoDate = null;
                if (userData.getFechaNacimiento() != null && !userData.getFechaNacimiento().isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    try {
                        fechaNacimientoDate = sdf.parse(userData.getFechaNacimiento());
                    } catch (ParseException e) {
                    }
                }

                Date fechaRegistroDate = null;
                if (userData.getFechaRegistro() != null && !userData.getFechaRegistro().isEmpty()) {
                    SimpleDateFormat sdfServerFormatRegistro = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    try {
                        fechaRegistroDate = sdfServerFormatRegistro.parse(userData.getFechaRegistro());
                    } catch (ParseException e) {

                    }
                }
                CachedUserEntity cachedUser = new CachedUserEntity(
                        userData.getId(),
                        userData.getUsername(),
                        userData.getNombre(),
                        userData.getApellido(),
                        userData.getImagenPerfilUrl(),
                        fechaNacimientoDate,
                        fechaRegistroDate,
                        "",
                        System.currentTimeMillis()
                );



                AppDatabase db = AppDatabase.getDatabase(getApplicationContext());

                Executors.newSingleThreadExecutor().execute(() -> {
                    try {
                        CachedUserDao cachedUserDao = db.cachedUserDao(); // O el nombre correcto de tu DAO
                        cachedUserDao.insertUser(cachedUser); // Asume OnConflictStrategy.REPLACE


                    } catch (Exception e) {
                        Log.e("LoginDB", "Error al guardar usuario en Room después del login: ", e);
                    }
                });
            }

            @OptIn(markerClass = UnstableApi.class)
            private void autenticarLocal(String username, String contrasena) {
                new Thread(() -> {
                    AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
                    UsuarioDao dao = db.usuarioDao(); // Usando tu UsuarioDao


                    Usuario usuarioLocal = dao.obtenerUsuarioParaFallbackPorUsername(username);


                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (usuarioLocal != null) {
                            SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();

                            editor.putString(KEY_LOGGED_IN_USERNAME, usuarioLocal.username);

                            String nombreCompletoLocal = "";
                            if (usuarioLocal.nombre != null && !usuarioLocal.nombre.isEmpty()) {
                                nombreCompletoLocal += usuarioLocal.nombre;
                            }
                            if (usuarioLocal.apellidos != null && !usuarioLocal.apellidos.isEmpty()) {
                                if (!nombreCompletoLocal.isEmpty()) nombreCompletoLocal += " ";
                                nombreCompletoLocal += usuarioLocal.apellidos;
                            }
                            editor.putString(KEY_USER_NAME, nombreCompletoLocal.isEmpty() ? usuarioLocal.username : nombreCompletoLocal);
                            editor.putString(KEY_USER_PROFILE_IMAGE_URL, usuarioLocal.imagenPerfil);
                            editor.putBoolean(KEY_IS_LOGGED_IN, true);
                            editor.apply();

                            Toast.makeText(login_user.this, "Login local exitoso.", Toast.LENGTH_SHORT).show();
                            Intent iPrincipal = new Intent(login_user.this, navBarSideActivity.class);
                            iPrincipal.putExtra("USER_USERNAME", usuarioLocal.username);
                            startActivity(iPrincipal);
                            finish();
                        } else {
                            Toast.makeText(login_user.this, "Credenciales inválidas (local).", Toast.LENGTH_SHORT).show();
                        }
                    });
                }).start();
            }
        }





