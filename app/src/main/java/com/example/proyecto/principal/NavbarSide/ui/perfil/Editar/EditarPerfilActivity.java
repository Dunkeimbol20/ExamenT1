package com.example.proyecto.principal.NavbarSide.ui.perfil.Editar; // Asegúrate de que este sea el paquete correcto

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher; // Para manejar resultados de otras Activities
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.proyecto.R;
import com.example.proyecto.data.cache.AppDatabase;
import com.example.proyecto.data.cache.CachedUserDao;
import com.example.proyecto.data.cache.CachedUserEntity;
import com.google.android.material.textfield.TextInputEditText;



public class EditarPerfilActivity extends AppCompatActivity {
    private static final String TAG = "EditarPerfilActivity";

    private ImageView imageEditPerfil;
    private Button btnEditarFoto, btnCancelar, btnGuardar;
    private TextInputEditText editTextNombre, editTextApellido, editTextUsername;

    private AppDatabase db;
    private CachedUserDao cachedUserDao;
    // private ApiService apiService;

    private String currentUsernameLoggedIn;
    private CachedUserEntity currentUserData;

    // Para la selección/captura de imagen
    private Uri currentPhotoUri; // URI de la foto tomada con la cámara
    private Uri selectedImageUri; // URI de la imagen seleccionada de la galería o cámara
    private boolean deleteCurrentImage = false; // Flag para saber si se debe eliminar la foto

    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> takePictureLauncher;
    private ActivityResultLauncher<Intent> pickImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        imageEditPerfil = findViewById(R.id.ImageEditPerfil);
        btnEditarFoto = findViewById(R.id.BtnEditarFoto);
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextApellido = findViewById(R.id.editTextApellido);
        editTextUsername = findViewById(R.id.editTextUsername);
        // ELIMINADO: editTextSexo = findViewById(R.id.editTextSexo);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnGuardar = findViewById(R.id.btnGuardar);

        db = AppDatabase.getDatabase(getApplicationContext());
        cachedUserDao = db.cachedUserDao();
        // apiService = ApiClient.getApiService();

        currentUsernameLoggedIn = getIntent().getStringExtra("username");

        if (currentUsernameLoggedIn == null || currentUsernameLoggedIn.isEmpty()) {
            Toast.makeText(this, "Error: No se pudo identificar al usuario.", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Username no recibido en el Intent.");
            finish();
            return;
        }
    }
}