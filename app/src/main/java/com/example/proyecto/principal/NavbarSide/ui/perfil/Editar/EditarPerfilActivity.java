package com.example.proyecto.principal.NavbarSide.ui.perfil.Editar; // Asegúrate de que este sea el paquete correcto

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.example.proyecto.R;
import com.example.proyecto.bd.MyDatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class EditarPerfilActivity extends AppCompatActivity {
    private ImageView ImageEditPerfil;
    private Button BtnEditarFoto;
    private Button btnGuardar, btnCancelar; // Referencia al botón "Guardar"
    private EditText editTextNombre,editTextApellido,editTextUsername,editTextSexo;
    private MyDatabaseHelper dbHelper; // Instancia de tu clase de base de datos
    private String nombreUsuarioActual; // Para almacenar el nombre de usuario que se está editando

    private ActivityResultLauncher<Intent> takePictureLauncher;
    private ActivityResultLauncher<Intent> pickImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        ImageEditPerfil = findViewById(R.id.ImageEditPerfil);
        // Inicializar EditTexts
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextApellido = findViewById(R.id.editTextApellido);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextSexo = findViewById(R.id.editTextSexo);

        dbHelper = new MyDatabaseHelper(this);

        BtnEditarFoto = findViewById(R.id.BtnEditarFoto); // Botón "Editar Foto"
        btnGuardar = findViewById(R.id.btnGuardar); // Botón "Guardar"
        btnCancelar = findViewById(R.id.btnCancelar); // Botón "Cancelar"

        Intent intent = getIntent();
        if(intent!=null &&intent.hasExtra("username")){
            nombreUsuarioActual = intent.getStringExtra("username");
            cargarDatosUsuario(nombreUsuarioActual);
        } else {
        Toast.makeText(this, "Error: No se especificó el usuario a editar", Toast.LENGTH_SHORT).show();
        finish();
        }

        // Inicializar los ActivityResultLaunchers
        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        ImageEditPerfil.setImageBitmap(imageBitmap);

                        // Convertir Bitmap a byte[]
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                        byte[] imageData = bos.toByteArray();

                        // ¡Llamar al método de base de datos para actualizar solo la imagen!
                        actualizarImagenPerfil(nombreUsuarioActual, imageData);

                        Toast.makeText(this, "Foto tomada exitosamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Error al tomar la foto", Toast.LENGTH_SHORT).show();
                    }
                });


        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        ImageEditPerfil.setImageURI(selectedImageUri);
                        try {
                            InputStream is = getContentResolver().openInputStream(selectedImageUri);
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            byte[] buffer = new byte[1024];
                            int len;
                            while ((len = is.read(buffer)) != -1) {
                                bos.write(buffer, 0, len);
                            }
                            byte[] imageData = bos.toByteArray();

                            is.close();
                            bos.close();

                            actualizarImagenPerfil(nombreUsuarioActual, imageData);

                            Toast.makeText(this, "Imagen seleccionada exitosamente", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Error al procesar la imagen", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Error al seleccionar la imagen", Toast.LENGTH_SHORT).show();
                    }
                });


        BtnEditarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarOpcionesImagen();
            }
        });


        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCambiosPerfil();
                // TODO: Implementar la lógica para guardar los cambios del perfil
                Toast.makeText(EditarPerfilActivity.this, "Guardar clicked", Toast.LENGTH_SHORT).show();
            }
        });

        btnGuardar.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gris_activo)); // Ejemplo con un color de recursos
        btnGuardar.setTextColor(ContextCompat.getColor(this, R.color.black)); // Ejemplo con un color de recursos

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botonCancelar();
            }
        });
    }




    private void mostrarOpcionesImagen() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Elige una opción");

        String[] opciones = {"Tomar foto", "Seleccionar de la galería", "Eliminar foto"};

        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        tomarFoto();
                        break;
                    case 1:
                        seleccionarDeGaleria();
                        break;
                    case 2:
                        eliminarFoto();
                        break;
                }
            }
        });

        builder.show();
    }

    private void tomarFoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            takePictureLauncher.launch(takePictureIntent);
        } else {
            Toast.makeText(this, "No hay aplicación de cámara disponible", Toast.LENGTH_SHORT).show();
        }
    }

    private void seleccionarDeGaleria() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(pickPhotoIntent);
    }

    private void eliminarFoto() {
        dbHelper.deleteProfilePicture(nombreUsuarioActual); // Llama al nuevo método en MyDatabaseHelper
        ImageEditPerfil.setImageResource(R.drawable.user); // Mostrar imagen por defecto en la UI
        Toast.makeText(this, "Foto de perfil eliminada", Toast.LENGTH_SHORT).show();
    }

    private void botonCancelar() {
        Toast.makeText(this, "Volviendo al perfil", Toast.LENGTH_SHORT).show();
        // Simplemente cierra esta actividad para regresar a la anterior
        finish();
    }

    private void cargarDatosUsuario(String username) {
        // Usa tu MyDatabaseHelper para obtener los datos del usuario
        Map<String, Object> userData = dbHelper.getUserData(username);

        if (userData != null) {

            if (userData.containsKey("nombre")) {
                editTextNombre.setText((String) userData.get("nombre"));
            }
            if (userData.containsKey("apellido")) {
                editTextApellido.setText((String) userData.get("apellido"));
            }
            if (userData.containsKey("username")) {
                editTextUsername.setText((String) userData.get("username"));
                editTextUsername.setEnabled(false); // Opcional: Deshabilitar edición
                editTextUsername.setFocusable(false); // Opcional: Deshabilitar enfoque
            }
            if (userData.containsKey("fecha_nacimiento")) {
                editTextSexo.setText((String) userData.get("fecha_nacimiento"));
            }

            if (userData.containsKey("imagen_perfil")) {
                byte[] imagenBytes = (byte[]) userData.get("imagen_perfil");
                if (imagenBytes != null && imagenBytes.length > 0) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imagenBytes, 0, imagenBytes.length);
                    ImageEditPerfil.setImageBitmap(bitmap);
                } else {
                    ImageEditPerfil.setImageResource(R.drawable.user);
                }
            } else {
                ImageEditPerfil.setImageResource(R.drawable.user);
            }



        } else {
            Toast.makeText(this, "Error al cargar los datos del usuario", Toast.LENGTH_SHORT).show();
        }
    }
    private void guardarCambiosPerfil() {
        String nuevoNombre = editTextNombre.getText().toString().trim();
        String nuevoApellido = editTextApellido.getText().toString().trim();
        String nuevoUsername = editTextUsername.getText().toString().trim();
        String nuevoSexo = editTextSexo.getText().toString().trim();


        if (nuevoNombre.isEmpty() || nuevoApellido.isEmpty() || nuevoUsername.isEmpty() ) {
            Toast.makeText(this, "Por favor, completa todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> userDetailsToUpdate = new HashMap<>();
        userDetailsToUpdate.put("nombre", nuevoNombre);
        userDetailsToUpdate.put("apellido", nuevoApellido);


        int rowsAffected = dbHelper.updateUserDetails(nombreUsuarioActual, userDetailsToUpdate);


        if (rowsAffected > 0) {
            Toast.makeText(this, "Perfil actualizado exitosamente", Toast.LENGTH_SHORT).show();
        } else {
            if (rowsAffected == 0) {
                Toast.makeText(this, "No se realizaron cambios en el perfil", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al actualizar el perfil", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void actualizarImagenPerfil(String username, byte[] imageData) {
        int rowsAffected = dbHelper.updateProfilePicture(username, imageData);

        if (rowsAffected > 0) {
            Toast.makeText(this, "Imagen de perfil actualizada", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al actualizar la imagen de perfil", Toast.LENGTH_SHORT).show();
        }
    }


}