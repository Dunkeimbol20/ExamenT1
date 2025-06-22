package com.example.proyecto.principal.NavbarSide.ui.perfil.AgregarReceta;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.proyecto.R;
import com.example.proyecto.data.cache.AppDatabase;
import com.example.proyecto.data.cache.RecetaEntity;
import com.example.proyecto.data.local.dao.RecetaDao;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AgregarRecetaActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private TextInputEditText editTextTituloReceta;
    private TextInputEditText editTextIngredientesReceta;
    private TextInputEditText editTextPreparacionReceta;
    // Cambiamos Spinner por AutoCompleteTextView
    private AutoCompleteTextView spinnerCategoriasReceta;
    private AutoCompleteTextView spinnerDificultadReceta;
    private AutoCompleteTextView spinnerEstacionReceta;
    private AutoCompleteTextView spinnerTiempoReceta;
    private AutoCompleteTextView spinnerCostoReceta;

    private ImageView imageViewReceta;
    private Button btnCargarImagen;
    private Button btnGuardarReceta;
    private Button btnCancelarReceta;

    private RecetaDao recetaDao;
    private ExecutorService databaseWriteExecutor;
    private String loggedInUsername;
    private Uri imagenUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_receta);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar vistas
        editTextTituloReceta = findViewById(R.id.editTextTituloReceta);
        editTextIngredientesReceta = findViewById(R.id.editTextIngredientesReceta);
        editTextPreparacionReceta = findViewById(R.id.editTextPreparacionReceta);

        spinnerCategoriasReceta = findViewById(R.id.spinnerCategoriasReceta);
        spinnerDificultadReceta = findViewById(R.id.spinnerDificultadReceta);
        spinnerEstacionReceta = findViewById(R.id.spinnerEstacionReceta);
        spinnerTiempoReceta = findViewById(R.id.spinnerTiempoReceta);
        spinnerCostoReceta = findViewById(R.id.spinnerCostoReceta);

        imageViewReceta = findViewById(R.id.imageViewReceta);
        btnCargarImagen = findViewById(R.id.btnCargarImagen);
        btnGuardarReceta = findViewById(R.id.GuardarReceta);
        btnCancelarReceta = findViewById(R.id.CancelarReceta);

        // Configurar Spinners (AutoCompleteTextViews)
        setupSpinners();

        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        recetaDao = db.recetaDao();
        databaseWriteExecutor = Executors.newSingleThreadExecutor();

        SharedPreferences preferences = getSharedPreferences("MiAppPrefs", Context.MODE_PRIVATE);
        loggedInUsername = preferences.getString("logged_in_username", null);

        if (loggedInUsername == null || loggedInUsername.isEmpty()) {
            Toast.makeText(this, "Error: No hay usuario logueado. No se puede guardar la receta.", Toast.LENGTH_LONG).show();
            btnGuardarReceta.setEnabled(false);
        }

        btnCargarImagen.setOnClickListener(v -> abrirGaleria());

        btnGuardarReceta.setOnClickListener(v -> {
            if (loggedInUsername == null || loggedInUsername.isEmpty()) {
                Toast.makeText(AgregarRecetaActivity.this, "Error: Usuario no identificado.", Toast.LENGTH_SHORT).show();
                return;
            }
            intentarGuardarReceta();
        });
        btnCancelarReceta.setOnClickListener(v -> finish());
    }
private void setupSpinners() {
    // Ejemplo para Categorías
    String[] categoriasArray = getResources().getStringArray(R.array.Categorias); // Asume que tienes este array en strings.xml
    ArrayAdapter<String> categoriasAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categoriasArray);
    spinnerCategoriasReceta.setAdapter(categoriasAdapter);

    // Ejemplo para Dificultad
    String[] dificultadArray = getResources().getStringArray(R.array.Dificultad); // Asume que tienes este array en strings.xml
    ArrayAdapter<String> dificultadAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, dificultadArray);
    spinnerDificultadReceta.setAdapter(dificultadAdapter);

    // Ejemplo para Estación
    String[] estacionArray = getResources().getStringArray(R.array.Estacion); // Asume que tienes este array en strings.xml
    ArrayAdapter<String> estacionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, estacionArray);
    spinnerEstacionReceta.setAdapter(estacionAdapter);

    // Ejemplo para Tiempo
    String[] tiempoArray = getResources().getStringArray(R.array.Tiempo); // Asume que tienes este array en strings.xml
    ArrayAdapter<String> tiempoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, tiempoArray);
    spinnerTiempoReceta.setAdapter(tiempoAdapter);

    // Ejemplo para Costo
    String[] costoArray = getResources().getStringArray(R.array.Dinero); // Asume que tienes este array en strings.xml
    ArrayAdapter<String> costoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, costoArray);
    spinnerCostoReceta.setAdapter(costoAdapter);

    // Opcional: poner un valor por defecto si es necesario (el primero del array)
    // if (categoriasArray.length > 0) spinnerCategoriasReceta.setText(categoriasAdapter.getItem(0), false);
    // ... y así para los demás ...
}
private void abrirGaleria() {
    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    startActivityForResult(intent,PICK_IMAGE_REQUEST);
}
@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
        imagenUri = data.getData();
        imageViewReceta.setImageURI(imagenUri); // Mostrar la imagen seleccionada
        Log.d("AgregarReceta", "Imagen URI: " + imagenUri.toString());
    }
}
    private void intentarGuardarReceta() {
        String titulo = editTextTituloReceta.getText().toString().trim();
        String ingredientes = editTextIngredientesReceta.getText().toString().trim();
        String preparacion = editTextPreparacionReceta.getText().toString().trim();

        // Obtener texto de los AutoCompleteTextView
        String dificultad = spinnerDificultadReceta.getText().toString();
        String estacion = spinnerEstacionReceta.getText().toString();
        String tiempo = spinnerTiempoReceta.getText().toString();
        String costo = spinnerCostoReceta.getText().toString();
        String categorias = spinnerCategoriasReceta.getText().toString();

        String imagenPathParaDb;

        if (imagenUri != null) {
            // ADVERTENCIA: Guardar la URI de la galería directamente no es robusto.
            // Considera copiar la imagen al almacenamiento interno de tu app.
            // Ejemplo: imagenPathParaDb = guardarImagenEnAlmacenamientoInternoYObtenerPath(imagenUri);
            imagenPathParaDb = imagenUri.toString(); // Para pruebas, pero revisa la advertencia.
            Log.d("AgregarReceta", "Usando imagen URI para la DB: " + imagenPathParaDb);
        } else {
            imagenPathParaDb = ""; // Cadena vacía o un placeholder si no hay imagen
            Log.d("AgregarReceta", "No se seleccionó imagen.");
        }

        // Validación más completa
        if (titulo.isEmpty()) {
            editTextTituloReceta.setError("El título es obligatorio");
            editTextTituloReceta.requestFocus();
            Toast.makeText(this, "El título es obligatorio.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (ingredientes.isEmpty()) {
            editTextIngredientesReceta.setError("Los ingredientes son obligatorios");
            editTextIngredientesReceta.requestFocus();
            Toast.makeText(this, "Los ingredientes son obligatorios.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (preparacion.isEmpty()) {
            editTextPreparacionReceta.setError("La preparación es obligatoria");
            editTextPreparacionReceta.requestFocus();
            Toast.makeText(this, "La preparación es obligatoria.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (categorias.isEmpty()) {
            // Para AutoCompleteTextView, puedes poner el error en el TextInputLayout si lo tienes referenciado
            // o simplemente un Toast.
            Toast.makeText(this, "Selecciona una categoría.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (dificultad.isEmpty()) {
            Toast.makeText(this, "Selecciona la dificultad.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (estacion.isEmpty()) {
            Toast.makeText(this, "Selecciona la estación.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tiempo.isEmpty()) {
            Toast.makeText(this, "Selecciona el tiempo de preparación.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (costo.isEmpty()) {
            Toast.makeText(this, "Selecciona el costo.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Puedes añadir una validación para imagenPathParaDb si la imagen es obligatoria

        RecetaEntity nuevaReceta = new RecetaEntity(
                titulo,
                imagenPathParaDb,
                dificultad,
                estacion,
                tiempo,
                costo,
                ingredientes,
                preparacion,
                categorias,
                loggedInUsername
        );

        databaseWriteExecutor.execute(() -> {
            try {
                long idInsertado = recetaDao.insertReceta(nuevaReceta);
                runOnUiThread(() -> {
                    if (idInsertado > -1) {
                        Toast.makeText(AgregarRecetaActivity.this, "¡Receta guardada con éxito!", Toast.LENGTH_LONG).show();
                        Log.d("AgregarReceta", "Receta guardada en Room con ID: " + idInsertado);
                        // Opcional: Devolver un resultado a la actividad anterior si es necesario
                        // Intent resultIntent = new Intent();
                        // setResult(Activity.RESULT_OK, resultIntent);
                        finish(); // Cierra la actividad después de guardar
                    } else {
                        Toast.makeText(AgregarRecetaActivity.this, "Error al guardar la receta en la base de datos.", Toast.LENGTH_LONG).show();
                        Log.e("AgregarReceta", "Error al insertar receta en Room, ID devuelto: " + idInsertado);
                    }
                });
            } catch (Exception e) {
                Log.e("AgregarReceta", "Excepción al guardar receta en Room", e);
                runOnUiThread(() -> Toast.makeText(AgregarRecetaActivity.this, "Error crítico al guardar: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }



    //BOTONES
    private void limpiarCampos() {
        editTextTituloReceta.setText("");
        editTextIngredientesReceta.setText("");
        editTextPreparacionReceta.setText("");
        // Resetear Spinners a la primera posición
        spinnerDificultadReceta.setSelection(0);
        spinnerEstacionReceta.setSelection(0);
        spinnerTiempoReceta.setSelection(0);
        spinnerCostoReceta.setSelection(0);
        // Limpiar la imagen mostrada
        imageViewReceta.setImageDrawable(null);
    }
}