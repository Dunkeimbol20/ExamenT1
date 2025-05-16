
package com.example.proyecto.principal.NavbarSide;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.proyecto.bd.MyDatabaseHelper;
import com.google.android.material.navigation.NavigationView;

import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import com.example.proyecto.R;
import com.example.proyecto.databinding.ActivityNavBarSidePrincipalBinding;
import com.example.proyecto.login_user.login_user;

import java.util.Map;

public class navBarSideActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavBarSidePrincipalBinding binding;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    private MyDatabaseHelper dbHelper;
    private String loggedInUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNavBarSidePrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarNavBarSide.toolbar);


        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        dbHelper = new MyDatabaseHelper(this);

        SharedPreferences preferences = getSharedPreferences("MiAppPrefs", Context.MODE_PRIVATE);
        loggedInUsername = preferences.getString("logged_in_username", null);
        displayUserInfoInNavDrawer();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_perfil, R.id.nav_recipes, R.id.nav_config, R.id.nav_cerrar_sesion)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_nav_bar_side);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_cerrar_sesion) {
                    cerrarSesion();
                    return true;
                }

                boolean handled = NavigationUI.onNavDestinationSelected(item, navController);

                drawer.closeDrawers();

                return handled;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_bar_side, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_nav_bar_side);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void cerrarSesion() {
        Intent intent = new Intent(this, login_user.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Toast.makeText(this, "Cerrando sesion", Toast.LENGTH_SHORT).show();
        finish();
    }

    @SuppressLint("SetTextI18n")
    private void displayUserInfoInNavDrawer() {
        if (loggedInUsername != null && !loggedInUsername.isEmpty()) {
            // <<-- Usa getUserData que devuelve el Map con la imagen
            Map<String, Object> userData = dbHelper.getUserData(loggedInUsername);

            // Encuentra la vista del encabezado
            View headerView = navigationView.getHeaderView(0);

            TextView textViewName = headerView.findViewById(R.id.navHeaderUserName);
            TextView textViewUsername = headerView.findViewById(R.id.navHeaderUsername);
            ImageView imageViewProfile = headerView.findViewById(R.id.navHeaderUserProfileImage);


            if (textViewName != null && textViewUsername != null && imageViewProfile != null && userData != null) {
                String userName = (String) userData.get("nombre");
                String userUsername = (String) userData.get("username"); // Puedes usar el de prefs o este

                byte[] imagenBytes = (byte[]) userData.get("imagen_perfil");

                if (userName != null && !userName.isEmpty()) {
                    textViewName.setText(userName);
                } else {
                    textViewName.setText("Usuario");
                }

                if (userUsername != null && !userUsername.isEmpty()) {
                    textViewUsername.setText("@"+userUsername);
                } else {
                    textViewUsername.setText(loggedInUsername != null ? loggedInUsername : "");
                }

                // <<-- Muestra la imagen de perfil
                if (imagenBytes != null && imagenBytes.length > 0) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imagenBytes, 0, imagenBytes.length);
                    if (bitmap != null) {
                        imageViewProfile.setImageBitmap(bitmap);
                    } else {
                        imageViewProfile.setImageResource(R.mipmap.ic_launcher_round);
                    }
                } else {
                    imageViewProfile.setImageResource(R.mipmap.ic_launcher_round);
                }

            } else {
                if (textViewName != null) textViewName.setText("Usuario Invitado");
                if (textViewUsername != null) textViewUsername.setText("");
                if (imageViewProfile != null)
                    imageViewProfile.setImageResource(R.mipmap.ic_launcher_round);
            }
        } else {
            View headerView = navigationView.getHeaderView(0);
            TextView textViewName = headerView.findViewById(R.id.navHeaderUserName);
            TextView textViewUsername = headerView.findViewById(R.id.navHeaderUsername);
            ImageView imageViewProfile = headerView.findViewById(R.id.navHeaderUserProfileImage);

            if (textViewName != null) textViewName.setText("Usuario Invitado");
            if (textViewUsername != null)
                textViewUsername.setText("Inicia sesión");
            if (imageViewProfile != null)
                imageViewProfile.setImageResource(R.mipmap.ic_launcher_round);
        }
    }
}