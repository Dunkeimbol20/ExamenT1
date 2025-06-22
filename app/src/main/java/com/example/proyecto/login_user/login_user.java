package com.example.proyecto.login_user;

import android.content.Intent;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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
                        // Login exitoso y tenemos datos del usuario
                        Log.d("Login", "Login exitoso con Retrofit: " + userData.toString());
                        // Aquí guardarías el usuario en Room usando userData
                        guardarUsuarioEnRoom(userData);

                        // Navegar a la siguiente actividad
                        Intent iPrincipal = new Intent(login_user.this, navBarSideActivity.class);
                        iPrincipal.putExtra("USER_USERNAME", userData.getUsername());
                        // También puedes pasar otros datos si es necesario
                        // iPrincipal.putExtra("USER_NOMBRE", userData.getNombre());
                        startActivity(iPrincipal);
                        finish();

                    } else {
                        // El servidor respondió OK, pero no hay datos de usuario o username es nulo
                        Log.e("Login", "Respuesta exitosa pero sin datos de usuario o username nulo.");
                        Toast.makeText(login_user.this, "Error en los datos recibidos del servidor.", Toast.LENGTH_LONG).show();
                        // Considera llamar a autenticarLocal aquí si es tu lógica de fallback
                        autenticarLocal(username, contrasena);
                    }
                } else {
                    // Error en la respuesta del servidor (4xx, 5xx, etc.)
                    // Podrías intentar parsear el errorBody si tu API devuelve errores específicos en JSON
                    String errorMsg = "Credenciales inválidas o error del servidor.";
                    if (response.code() == 401) { // Unauthorized
                        errorMsg = "Usuario o contraseña incorrectos.";
                    }
                    Log.e("Login", "Error en login con Retrofit: " + response.code() + " - " + response.message());
                    Toast.makeText(login_user.this, errorMsg, Toast.LENGTH_LONG).show();
                    // Lógica de fallback si falla la autenticación online
                    autenticarLocal(username, contrasena);
                }
            }

            @OptIn(markerClass = UnstableApi.class)
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                // Fallo en la red o al procesar la petición/respuesta
                Log.e("Login", "Fallo en la llamada de login con Retrofit: " + t.getMessage(), t);
                Toast.makeText(login_user.this, "Error de conexión. Intentando autenticación local.", Toast.LENGTH_LONG).show();
                autenticarLocal(username, contrasena); // Fallback a la autenticación local
            }
        });
    }
            @OptIn(markerClass = UnstableApi.class)
            private void guardarUsuarioEnRoom(UserData userData) {
                // Validación crucial
                if (userData.getUsername() == null || userData.getUsername().trim().isEmpty()) {
                    Log.e("LoginDB", "Intento de guardar usuario con username nulo o vacío en Room.");
                    // Decide cómo manejar esto, quizás mostrar un error genérico
                    return;
                }

                Date fechaNacimientoDate = null;
                if (userData.getFechaNacimiento() != null && !userData.getFechaNacimiento().isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    try {
                        fechaNacimientoDate = sdf.parse(userData.getFechaNacimiento());
                    } catch (ParseException e) {
                        Log.e("LoginDB", "Error parseando fecha nacimiento para Room: " + userData.getFechaNacimiento(), e);
                    }
                }

                Date fechaRegistroDate = null;
                if (userData.getFechaRegistro() != null && !userData.getFechaRegistro().isEmpty()) {
                    SimpleDateFormat sdfServerFormatRegistro = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    try {
                        fechaRegistroDate = sdfServerFormatRegistro.parse(userData.getFechaRegistro());
                    } catch (ParseException e) {
                        Log.e("LoginDB", "Error al parsear fecha de registro (yyyy-MM-dd HH:mm:ss) para Room: " + userData.getFechaRegistro(), e);
                        // Intenta con otro formato si el primero falla, por ejemplo, solo fecha
                        try {
                            sdfServerFormatRegistro = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            fechaRegistroDate = sdfServerFormatRegistro.parse(userData.getFechaRegistro());
                        } catch (ParseException ex) {
                            Log.e("LoginDB", "Error al parsear fecha de registro (yyyy-MM-dd) para Room: " + userData.getFechaRegistro(), ex);
                        }
                    }
                }

                // Aquí debes decidir qué entidad de Room usar.
                // Si tienes una CachedUserEntity como en RegisterUser:
                CachedUserEntity cachedUser = new CachedUserEntity(
                        userData.getId(),
                        userData.getUsername(), // Este es el importante
                        userData.getNombre(),
                        userData.getApellido(),
                        userData.getImagenPerfilUrl(),
                        fechaNacimientoDate,
                        fechaRegistroDate,
                        "",
                        System.currentTimeMillis()
                );

                Log.d("LoginDB_CACHE_INSERT", "Intentando insertar/reemplazar en Room (Login)...");
                Log.d("LoginDB_CACHE_INSERT", "ServerID: " + cachedUser.getServerId());
                Log.d("LoginDB_CACHE_INSERT", "Username: " + cachedUser.getUsername());
                Log.d("LoginDB_CACHE_INSERT", "Nombre: " + cachedUser.getNombre());
                Log.d("LoginDB_CACHE_INSERT", "Apellido: " + cachedUser.getApellido());
                Log.d("LoginDB_CACHE_INSERT", "ImgURL: " + cachedUser.getImagenPerfilUrl());
                Log.d("LoginDB_CACHE_INSERT", "FechaNac: " + (cachedUser.getFechaNacimiento() != null ? cachedUser.getFechaNacimiento().toString() : "null"));
                Log.d("LoginDB_CACHE_INSERT", "FechaReg: " + (cachedUser.getFechaRegistro() != null ? cachedUser.getFechaRegistro().toString() : "null"));
                Log.d("LoginDB_CACHE_INSERT", "Token: " + cachedUser.getContrasenaOToken());
                Log.d("LoginDB_CACHE_INSERT", "TimestampCache: " + cachedUser.getFechaCacheadoTimestamp());
                // --- FIN DE LOGS DE DEPURACIÓN ---

                AppDatabase db = AppDatabase.getDatabase(getApplicationContext());

                Executors.newSingleThreadExecutor().execute(() -> {
                    try {
                        CachedUserDao cachedUserDao = db.cachedUserDao(); // O el nombre correcto de tu DAO
                        cachedUserDao.insertUser(cachedUser); // Asume OnConflictStrategy.REPLACE
                        Log.i("LoginDB_CACHE_INSERT", "Inserción/Reemplazo en Room (Login) completado para: " + cachedUser.getUsername());
                        //Log.d("LoginDB", "Usuario " + userData.getUsername() + " guardado/actualizado en Room después del login.");
                        // --- INICIO DE PRUEBA DE LECTURA ---
                        Log.d("LoginDB_CACHE_READ", "Intentando leer de Room (Login): " + cachedUser.getUsername());
                        CachedUserEntity userFromDb = cachedUserDao.getUserByUsername(cachedUser.getUsername());

                        if (userFromDb != null) {
                            Log.i("LoginDB_CACHE_READ", "ÉXITO LECTURA (Login)! Usuario '" + userFromDb.getUsername() + "' encontrado en DB. Nombre: " + userFromDb.getNombre());
                        } else {
                            Log.e("LoginDB_CACHE_READ", "FALLO LECTURA (Login)! Usuario '" + cachedUser.getUsername() + "' NO encontrado en DB después de insertar.");
                        }
                        // --- FIN DE PRUEBA DE LECTURA ---
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
                    ;


                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (usuarioLocal != null) {
                            // Si la autenticación local no verifica contraseña, y solo username:
                            // Deberías también verificar la contraseña aquí si la tienes hasheada localmente.
                            // Este es un ejemplo simple donde solo la existencia del username en local cuenta.
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





