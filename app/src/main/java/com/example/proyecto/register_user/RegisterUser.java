package com.example.proyecto.register_user;

import static android.widget.Toast.LENGTH_LONG;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.proyecto.R;
import com.example.proyecto.login_user.login_user;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {
    Button botonCancelar,botonGuardar;
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
        botonCancelar = findViewById(R.id.btnCancelar);
        botonGuardar = findViewById(R.id.btnGuardar);
        botonCancelar.setOnClickListener(this);
        botonGuardar.setOnClickListener(this);
    }
    public void onClick(View v) {
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