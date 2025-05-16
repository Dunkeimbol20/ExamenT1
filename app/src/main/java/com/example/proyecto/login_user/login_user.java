package com.example.proyecto.login_user;

import static com.example.proyecto.bd.MyDatabaseHelper.DATABASE_NAME;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.proyecto.R;
import com.example.proyecto.bd.MyDatabaseHelper;
import com.example.proyecto.principal.NavbarSide.navBarSideActivity;
import com.example.proyecto.principal.NavbarSide.ui.home.HomeFragment;
import com.example.proyecto.register_user.RegisterUser;

public class login_user extends AppCompatActivity implements View.OnClickListener {
    Button botonRegisterUser,botonLoginUser;
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
        botonRegisterUser.setOnClickListener(this);
        //Datos de verificacion
        EditText LoginEditTextUsername = findViewById(R.id.LoginEditTextUsername);
        EditText LoginEditTextContrasena = findViewById(R.id.LoginEditTextContrasena);
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
        //verificar
        botonLoginUser.setOnClickListener(v -> {
            String username = LoginEditTextUsername.getText().toString().trim();
            String contrasena = LoginEditTextContrasena.getText().toString().trim();
            if (username.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(login_user.this, "Por favor ingresa los datos completos", Toast.LENGTH_SHORT).show();
            }else {
                if (dbHelper.verificarLogin(username, contrasena)) {
                    SharedPreferences preferences = getSharedPreferences("MiAppPrefs", Context.MODE_PRIVATE); // "MiAppPrefs" es el nombre del archivo

                    // 2. Obtener un Editor para escribir en SharedPreferences
                    SharedPreferences.Editor editor = preferences.edit();

                    // 3. Guardar el username con la clave "logged_in_username"
                    editor.putString("logged_in_username", username); // 'username' es la variable que contiene el username ingresado

                    // 4. Confirmar los cambios (apply() es asíncrono, commit() es síncrono)
                    editor.apply();

                    Toast.makeText(this, "Login correcto", Toast.LENGTH_SHORT).show();
                    // Aquí podrías abrir otra actividad y pasarle el nombre, etc.
                    Intent iPrincipal = new Intent(this, navBarSideActivity.class);
                    iPrincipal.putExtra("username", username);
                    startActivity(iPrincipal);
                    finish();

                } else {
                    Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        /*btnEliminarBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarBaseDeDatos(getApplicationContext());
            }
        });*/
    }

    @Override

    public void onClick(View v) {
        if(v.getId()== R.id.btnRegisterUser)
            registro();
        /*
        if(v.getId()==R.id.btnLoginUser)
            inicio();*/
        //caso contrario ,ingresa el boton INGRESAR ,valida la cuenta y accede ,por lo tanto ,se visualizara en el Componente Principal(Main)
    }


    private void registro() {
        Intent iRegistro = new Intent(this, RegisterUser.class);
        startActivity(iRegistro);
    }
    public void eliminarBaseDeDatos(Context context) {
        boolean eliminada = context.deleteDatabase(DATABASE_NAME);

        if (eliminada) {
            Log.d("BaseDeDatos", "Base de datos eliminada correctamente.");
        } else {
            Log.d("BaseDeDatos", "No se pudo eliminar la base de datos.");
        }
    }



}