// En tu Activity principal con el Navigation Drawer (ej: navBarSideActivity.java)

package com.example.proyecto.principal.NavbarSide; // Ajusta el paquete si es necesario

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem; // Importa MenuItem

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.example.proyecto.R;
import com.example.proyecto.databinding.ActivityNavBarSidePrincipalBinding;
import com.example.proyecto.login_user.login_user; // Importa tu Activity de Login

public class navBarSideActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavBarSidePrincipalBinding binding;
    private DrawerLayout drawer; // Referencia al DrawerLayout
    private NavigationView navigationView; // Referencia al NavigationView


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNavBarSidePrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarNavBarSide.toolbar);


        drawer = binding.drawerLayout; // Obtén la referencia al DrawerLayout
        navigationView = binding.navView; // Obtén la referencia al NavigationView

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_perfil, R.id.nav_recipes,R.id.nav_config,R.id.nav_cerrar_sesion)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_nav_bar_side);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // --- Manejar clics en los elementos del menú de navegación ---
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Manejar la selección del ítem aquí
                int id = item.getItemId();

                if (id == R.id.nav_cerrar_sesion) {
                    // Acción para Cerrar Sesión
                    cerrarSesion();
                    // Indica que el evento ha sido manejado
                    return true;
                }

                // Si el ID no es "Cerrar Sesión", deja que el NavigationUI lo maneje
                // para navegar a los fragments del Drawer.
                boolean handled = NavigationUI.onNavDestinationSelected(item, navController);

                // Cierra el drawer después de la selección
                drawer.closeDrawers();

                return handled; // Retorna true si el NavigationUI manejó la selección, false en caso contrario
            }
        });
        // --- Fin del manejo de clics en los elementos del menú de navegación ---

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_bar_side, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_nav_bar_side);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // Método para cerrar sesión
    private void cerrarSesion() {
        // TODO: Aquí debes implementar la lógica real para cerrar la sesión
        // Esto podría incluir:
        // 1. Limpiar datos de sesión (por ejemplo, Shared Preferences, base de datos local).
        // 2. Invalidar tokens de autenticación si usas alguno.
        // 3. Cualquier otra limpieza necesaria.

        // Por ahora, simplemente navegamos a la pantalla de login
        Intent intent = new Intent(this, login_user.class);
        // Opcional: Limpiar el historial de actividades para que el usuario no pueda regresar a la Activity principal
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Toast.makeText(this,"Cerrando sesion",Toast.LENGTH_SHORT).show();
        // Finalizar esta Activity para que el usuario no pueda regresar con el botón de atrás
        finish();
    }
}