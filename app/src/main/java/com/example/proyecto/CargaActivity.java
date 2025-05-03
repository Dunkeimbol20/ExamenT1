package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.proyecto.login_user.login_user;

public class CargaActivity extends AppCompatActivity {
    ProgressBar barCarga;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_carga);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        barCarga = findViewById(R.id.carBarCarga);
        Thread tCarga = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= barCarga.getMax() ; i++) {
                    barCarga.setProgress(i);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                Intent iSesion = new Intent(getApplicationContext(), login_user.class);
                startActivity(iSesion);
                finish();
            }
        });
        tCarga.start();
    }
}