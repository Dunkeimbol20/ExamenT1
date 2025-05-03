package com.example.proyecto.login_user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.proyecto.R;
import com.example.proyecto.principal.NavbarSide.navBarSideActivity;
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
        botonLoginUser = findViewById(R.id.btnLoginUser);
        //Asignar eventos a los botones

        botonRegisterUser.setOnClickListener(this);
        botonLoginUser.setOnClickListener(this);
    }

    @Override

    public void onClick(View v) {
        if(v.getId()== R.id.btnRegisterUser)
            registro();
        if(v.getId()==R.id.btnLoginUser)
            inicio();
        //caso contrario ,ingresa el boton INGRESAR ,valida la cuenta y accede ,por lo tanto ,se visualizara en el Componente Principal(Main)
    }


    private void registro() {
        Intent iRegistro = new Intent(this, RegisterUser.class);
        startActivity(iRegistro);
    }

    private void inicio() {
        Intent iPrincipal = new Intent(this, navBarSideActivity.class);
        startActivity(iPrincipal);
    }

}