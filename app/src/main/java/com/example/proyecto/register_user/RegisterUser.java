package com.example.proyecto.register_user;

import static android.widget.Toast.LENGTH_LONG;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.proyecto.R;
import com.example.proyecto.bd.MyDatabaseHelper;
import com.example.proyecto.login_user.login_user;

import java.time.Year;
import java.time.YearMonth;
import java.util.Calendar;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    Button botonCancelar,botonGuardar;
    //Fecha
    Button bfecha;
    EditText editTextfecha;
    private int dia,mes,ano;
    //Base de datos
    EditText editTextNombre, editTextApellido, editTextUsername, editTextContrasena;
    MyDatabaseHelper dbHelper;
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
        //fecha
        bfecha = findViewById(R.id.btnFecha);
        editTextfecha = findViewById(R.id.EditTextFecha);
        bfecha.setOnClickListener(this);

        //general
        botonCancelar = findViewById(R.id.btnCancelar);
        botonGuardar = findViewById(R.id.btnGuardar);
        botonCancelar.setOnClickListener(this);
        botonGuardar.setOnClickListener(this);
        //base de datos
        editTextNombre = findViewById(R.id.EditTextNombre);
        editTextApellido = findViewById(R.id.EditTextApellido);
        editTextUsername = findViewById(R.id.EditTextUsername);
        editTextContrasena = findViewById(R.id.EditTextContrasena);
        dbHelper = new MyDatabaseHelper(this);
        //boton guardar
        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Username = editTextUsername.getText().toString().trim();
                String nombre = editTextNombre.getText().toString().trim();
                String apellidos = editTextApellido.getText().toString().trim();
                String contrasena = editTextContrasena.getText().toString().trim();
                String fecha = editTextfecha.getText().toString().trim();
            if (!nombre.isEmpty() && !Username.isEmpty()) {
                dbHelper.insertarUsuario(nombre, Username,apellidos,fecha,contrasena);
                Toast.makeText(RegisterUser.this, "Guardado correctamente", Toast.LENGTH_SHORT).show();
                editTextNombre.setText("");
                editTextUsername.setText("");
                editTextApellido.setText("");
                editTextContrasena.setText("");
                editTextfecha.setText("");
            } else {
                Toast.makeText(RegisterUser.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            }
            }
        });
    }
    public void onClick(View v) {
        if(v.getId()==R.id.btnFecha) {
            final Calendar c = Calendar.getInstance();
            dia = c.get(Calendar.DAY_OF_MONTH);
            mes = c.get(Calendar.MONTH);
            ano = c.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    editTextfecha.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                }
            }, dia, mes, ano);
            datePickerDialog.show();
        }
        if(v.getId()==R.id.btnCancelar) {
            cancelar();
        }else if(v.getId()==R.id.btnGuardar){
            Toast.makeText(this,"Registro actualizado",LENGTH_LONG).show();
        }
    }

    private void cancelar() {
        Intent iCancelar = new Intent(this, login_user.class);
        startActivity(iCancelar);
    }

}