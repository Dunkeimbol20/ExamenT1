package com.example.proyecto.register_user;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;
import com.example.proyecto.R;
import com.example.proyecto.data.UserData;
import com.example.proyecto.data.UserResponse;
import com.example.proyecto.data.cache.AppDatabase;
import com.example.proyecto.data.cache.CachedUserDao;
import com.example.proyecto.data.cache.CachedUserEntity;
import com.example.proyecto.data.remote.api.ApiService;
import com.example.proyecto.data.remote.client.RetrofitClient;
import com.example.proyecto.login_user.login_user;

import androidx.activity.EdgeToEdge;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterUser extends AppCompatActivity{
    private static final String TAG = "RegisterUser";

    Button botonCancelar, botonGuardar;
    Button btnFecha;
    private Calendar calendar;
    EditText editTextNombre, editTextApellido, editTextUsername, editTextContrasena, editTextFecha;
    // --- Room Database ---
    private AppDatabase appDatabaseRoom;
    private CachedUserDao cachedUserDao;
    private final ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();
    // --- Retrofit Service ---
    private ApiService apiService;
    private final String IMAGEN_PERFIL_URL_POR_DEFECTO = "https://lightskyblue-cod-920464.hostingersite.com/api/uploads/profiles_pictures/user_default.png";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        btnFecha = findViewById(R.id.btnFecha);
        calendar = Calendar.getInstance();
        botonCancelar = findViewById(R.id.btnCancelar);
        botonGuardar = findViewById(R.id.btnGuardar);
        //Campos de registro
        editTextNombre = findViewById(R.id.EditTextNombre);
        editTextApellido = findViewById(R.id.EditTextApellido);
        editTextFecha = findViewById(R.id.EditTextFecha);
        editTextUsername = findViewById(R.id.EditTextUsername);
        editTextContrasena = findViewById(R.id.EditTextContrasena);

        appDatabaseRoom = AppDatabase.getDatabase(getApplicationContext());
        cachedUserDao = appDatabaseRoom.cachedUserDao();
        apiService = RetrofitClient.getApiService();

        btnFecha.setOnClickListener(v -> mostrarDatePicker());
        botonGuardar.setOnClickListener(v -> intentarRegistrarUsuario());
        botonCancelar.setOnClickListener(v -> finish());
    }
    private void mostrarDatePicker() {
        int anio = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    String fechaSeleccionada = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    editTextFecha.setText(fechaSeleccionada);
                }, anio, mes, dia);
        datePickerDialog.show();
    }
    private String obtenerFechaHoy() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }
    private void intentarRegistrarUsuario() {
        String nombre = editTextNombre.getText().toString().trim();
        String apellido = editTextApellido.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();
        String contrasena = editTextContrasena.getText().toString().trim();
        String fechaNacStr = editTextFecha.getText().toString().trim(); // Formato YYYY-MM-DD

        // --- Validación de Campos ---
        if (nombre.isEmpty() || apellido.isEmpty() || username.isEmpty() || contrasena.isEmpty() || fechaNacStr.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- Deshabilitar botón para evitar múltiples clicks ---
        botonGuardar.setEnabled(false);
        Toast.makeText(this, "Registrando usuario...", Toast.LENGTH_SHORT).show(); // Feedback visual

        // --- Llamada a la API con Retrofit ---
        Call<UserResponse> call = apiService.registrarUsuario(
                nombre,
                apellido,
                username,
                contrasena,
                fechaNacStr,
                IMAGEN_PERFIL_URL_POR_DEFECTO // Enviamos la URL por defecto
        );

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                botonGuardar.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    UserResponse userResponse = response.body();
                    UserData userData = userResponse.getUser();

                    //Log.d(TAG, "Registro en servidor exitoso: " + userResponse.toString());
                    //Toast.makeText(RegisterUser.this, "Usuario registrado en servidor!", Toast.LENGTH_SHORT).show();
                    if (userData != null) { // <--- IMPORTANTE: Verificar que userData no sea null
                        Log.d(TAG, "Registro en servidor exitoso: " + userData.toString());
                        Toast.makeText(RegisterUser.this, "Usuario registrado en servidor!", Toast.LENGTH_SHORT).show();
                    }
                    cachearUsuarioDespuesDeRegistroExitoso(userData);

                } else {
                    Log.e(TAG, "Error en registro: UserData es null en la respuesta del servidor (dentro de UserResponse).");
                    // Manejo de errores de la API (ej: usuario ya existe, error del servidor, etc.)
                    String errorMessage = "Error en el registro del servidor";
                    if (response.errorBody() != null) {
                        try {
                            // Intenta parsear un mensaje de error del cuerpo de error, si tu API lo envía
                            errorMessage += ": " + response.errorBody().string();
                        } catch (Exception e) {
                            Log.e(TAG, "Error al parsear errorBody", e);
                        }
                    } else if (response.code() == 409) { // Ejemplo: 409 Conflict podría ser usuario duplicado
                        errorMessage = "El nombre de usuario ya existe.";
                    } else {
                        errorMessage += " (Código: " + response.code() + ")";
                    }
                    Log.e(TAG, "Error en registro: " + errorMessage);
                    Toast.makeText(RegisterUser.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                botonGuardar.setEnabled(true);
                Log.e(TAG, "Fallo en la llamada de registro: " + t.getMessage(), t);
                Toast.makeText(RegisterUser.this, "Fallo en la conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void cachearUsuarioDespuesDeRegistroExitoso(UserData userData) {


        // Convertir String de fecha de nacimiento (YYYY-MM-DD) del servidor a Date
        Date fechaNacimientoDate = null;
        if (userData.getFechaNacimiento() != null && !userData.getFechaNacimiento().isEmpty()) {
            SimpleDateFormat sdfServerFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            try {
                fechaNacimientoDate = sdfServerFormat.parse(userData.getFechaNacimiento());
            } catch (ParseException e) {
                Log.e(TAG, "Error al parsear fecha de nacimiento del servidor: " + userData.getFechaNacimiento(), e);
                // Decidir si continuar sin fecha o manejar el error de otra forma
            }
        }

        // Convertir String de fecha de registro del servidor a Date
        // El formato de 'fechaRegistro' dependerá de cómo lo envíe tu API PHP.
        // Asumamos que es "yyyy-MM-dd HH:mm:ss" o similar. Ajusta si es diferente.
        Date fechaRegistroDate = null;
        if (userData.getFechaRegistro() != null && !userData.getFechaRegistro().isEmpty()) {
            // Intenta con el formato más común primero
            SimpleDateFormat sdfServerFormatRegistro = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            try {
                fechaRegistroDate = sdfServerFormatRegistro.parse(userData.getFechaRegistro());
            } catch (ParseException e) {
                Log.e(TAG, "Error al parsear fecha de registro del servidor (formato yyyy-MM-dd HH:mm:ss): " + userData.getFechaRegistro(), e);
                // Intenta con otro formato si el primero falla, por ejemplo, solo fecha
                try {
                    sdfServerFormatRegistro = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    fechaRegistroDate = sdfServerFormatRegistro.parse(userData.getFechaRegistro());
                } catch (ParseException ex) {
                    Log.e(TAG, "Error al parsear fecha de registro del servidor (formato yyyy-MM-dd): " + userData.getFechaRegistro(), ex);
                }
            }
        }

        // Crear la entidad para Room
        CachedUserEntity cachedUser = new CachedUserEntity(
                userData.getId(), // server_id <- IMPORTANTE, es el ID de MySQL
                userData.getUsername(),
                userData.getNombre(),
                userData.getApellido(),
                userData.getImagenPerfilUrl(), // La URL que vino del servidor (o la que enviaste)
                fechaNacimientoDate, // Date object or null
                fechaRegistroDate,   // Date object or null (o puedes usar new Date() si prefieres la hora local del cacheo)
                "", // contrasenaOToken - No guardamos la contraseña en caché. Podría ser un token más adelante.
                System.currentTimeMillis() // fecha_cacheado_timestamp
        );
        // --- INICIO DE LOGS DE DEPURACIÓN ---
        Log.d("DB_INSERT_REGISTER", "Intentando insertar/reemplazar en Room...");
        Log.d("DB_INSERT_REGISTER", "ServerID: " + cachedUser.getServerId());
        Log.d("DB_INSERT_REGISTER", "Username: " + cachedUser.getUsername());
        Log.d("DB_INSERT_REGISTER", "Nombre: " + cachedUser.getNombre());
        Log.d("DB_INSERT_REGISTER", "Apellido: " + cachedUser.getApellido());
        Log.d("DB_INSERT_REGISTER", "ImgURL: " + cachedUser.getImagenPerfilUrl());
        Log.d("DB_INSERT_REGISTER", "FechaNac: " + (cachedUser.getFechaNacimiento() != null ? cachedUser.getFechaNacimiento().toString() : "null"));
        Log.d("DB_INSERT_REGISTER", "FechaReg: " + (cachedUser.getFechaRegistro() != null ? cachedUser.getFechaRegistro().toString() : "null"));
        Log.d("DB_INSERT_REGISTER", "Token: " + cachedUser.getContrasenaOToken());
        Log.d("DB_INSERT_REGISTER", "TimestampCache: " + cachedUser.getFechaCacheadoTimestamp());
        // --- FIN DE LOGS DE DEPURACIÓN ---

        databaseWriteExecutor.execute(() -> {
            try {

                cachedUserDao.insertUser(cachedUser);
                Log.i("DB_INSERT_REGISTER", "Inserción/Reemplazo en Room completado para: " + cachedUser.getUsername());
                // --- INICIO DE PRUEBA DE LECTURA ---
                Log.d("DB_READ_REGISTER", "Intentando leer de Room: " + cachedUser.getUsername());
                CachedUserEntity userFromDb = cachedUserDao.getUserByUsername(cachedUser.getUsername());

                if (userFromDb != null) {
                    Log.i("DB_READ_REGISTER", "ÉXITO LECTURA! Usuario '" + userFromDb.getUsername() + "' encontrado en DB. Nombre: " + userFromDb.getNombre());
                } else {
                    Log.e("DB_READ_REGISTER", "FALLO LECTURA! Usuario '" + cachedUser.getUsername() + "' NO encontrado en DB después de insertar.");
                }
                // --- FIN DE PRUEBA DE LECTURA ---

                Log.d(TAG, "Usuario cacheado en Room: " + cachedUser.getUsername());
                runOnUiThread(() -> {
                    Toast.makeText(RegisterUser.this, "Usuario cacheado localmente.", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                     Intent intent = new Intent(RegisterUser.this, login_user.class);
                     startActivity(intent);
                     finish();
                });
            } catch (Exception e) {
                Log.e(TAG, "Error al cachear usuario en Room: ", e);
                Log.e("DB_ERROR_REGISTER", "Error durante operación de DB (Insertar/Leer): ", e);
                runOnUiThread(() -> Toast.makeText(RegisterUser.this, "Error al guardar localmente: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }
    private void limpiarCampos() {
        editTextNombre.setText("");
        editTextApellido.setText("");
        editTextFecha.setText("");
        editTextUsername.setText("");
        editTextContrasena.setText("");
        calendar = Calendar.getInstance();
        editTextNombre.requestFocus();
    }
    protected void onDestroy() {
        super.onDestroy();
        if (databaseWriteExecutor != null && !databaseWriteExecutor.isShutdown()) {
            databaseWriteExecutor.shutdown();
        }
    }
}