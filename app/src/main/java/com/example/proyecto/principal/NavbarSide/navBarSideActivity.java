
package com.example.proyecto.principal.NavbarSide;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
public class navBarSideActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavBarSidePrincipalBinding binding;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    private String loggedInUsername;
    public static final String PREFS_NAME = "MiAppPrefs";
    public static final String KEY_LOGGED_IN_USERNAME = "logged_in_username";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_PROFILE_IMAGE_URL = "user_profile_image_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("NavBarSideActivity", "onCreate - INICIO");
        binding = ActivityNavBarSidePrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarNavBarSide.toolbar);


        drawer = binding.drawerLayout;
        navigationView = binding.navView;

        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        loggedInUsername = preferences.getString(KEY_LOGGED_IN_USERNAME, null);
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
                boolean handled = false;
                if (id == R.id.nav_cerrar_sesion) {
                    cerrarSesion();
                    handled = true;
                }else{
                    handled = NavigationUI.onNavDestinationSelected(item, navController);

                }
                if(handled){
                    drawer.closeDrawers();

                }
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
        // Limpiar SharedPreferences
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(KEY_LOGGED_IN_USERNAME);
        editor.remove(KEY_USER_NAME);
        editor.remove(KEY_USER_PROFILE_IMAGE_URL);
        editor.apply();

        Intent intent = new Intent(this, login_user.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Toast.makeText(this, "Cerrando sesión...", Toast.LENGTH_SHORT).show();
        finish();
    }
    private void displayUserInfoInNavDrawer() {
        View headerView = navigationView.getHeaderView(0);
        if (headerView == null) {
            Log.e("NavBarSideActivity", "HeaderView no encontrado en NavigationView");
            return;
        }

        TextView textViewName = headerView.findViewById(R.id.navHeaderUserName);
        TextView textViewUsername = headerView.findViewById(R.id.navHeaderUsername);
        ImageView imageViewProfile = headerView.findViewById(R.id.navHeaderUserProfileImage);

        if (textViewName == null || textViewUsername == null || imageViewProfile == null) {
            Log.e("NavBarSideActivity", "Alguna vista (nombre, username, imagen) no fue encontrada en el header.");
            // Aun así, intenta poner valores por defecto si algunas vistas existen
            if (textViewName != null) textViewName.setText("Usuario Invitado");
            if (textViewUsername != null) textViewUsername.setText("Inicia sesión");
            if (imageViewProfile != null) imageViewProfile.setImageResource(R.mipmap.ic_launcher_round); // Placeholder muy genérico
            return;
        }

        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        // loggedInUsername ya está disponible como variable de instancia
        String userNameFromPrefs = preferences.getString(KEY_USER_NAME, "Usuario");
        String profileImageUrlFromPrefs = preferences.getString(KEY_USER_PROFILE_IMAGE_URL, null);

        if (loggedInUsername != null && !loggedInUsername.isEmpty()) {
            // Usuario logueado
            Log.d("NavBarSideActivity", "Usuario logueado: " + loggedInUsername);
            Log.d("NavBarSideActivity", "Nombre desde Prefs: " + userNameFromPrefs);
            Log.d("NavBarSideActivity", "URL Imagen desde Prefs: " + profileImageUrlFromPrefs);

            textViewName.setText(userNameFromPrefs);
            textViewUsername.setText("@" + loggedInUsername);

            if (profileImageUrlFromPrefs != null && !profileImageUrlFromPrefs.isEmpty()) {
                Glide.with(this)
                        .load(profileImageUrlFromPrefs)
                        .placeholder(R.drawable.ic_profile_placeholder) // Debes crear este drawable
                        .error(R.drawable.user_default) // Tu user_default.png en drawables
                        .apply(RequestOptions.circleCropTransform()) // Para hacerla circular
                        .into(imageViewProfile);
            } else {
                // Si no hay URL en SharedPreferences o está vacía, muestra una imagen por defecto local
                Glide.with(this)
                        .load(R.drawable.user_default)
                        .apply(RequestOptions.circleCropTransform())
                        .into(imageViewProfile);
                Log.d("NavBarSideActivity", "URL de imagen de perfil no encontrada en SharedPreferences, usando imagen por defecto.");
            }
        } else {
            // Usuario no logueado
            Log.d("NavBarSideActivity", "Usuario no logueado, mostrando información de invitado.");
            textViewName.setText("Usuario Invitado");
            textViewUsername.setText("Inicia sesión");
            Glide.with(this)
                    .load(R.drawable.user)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageViewProfile);
        }
    }
}