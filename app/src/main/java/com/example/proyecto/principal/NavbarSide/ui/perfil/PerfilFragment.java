package com.example.proyecto.principal.NavbarSide.ui.perfil;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.proyecto.R;
import com.example.proyecto.databinding.FragmentPerfilBinding;
import com.example.proyecto.principal.Buttom.Category.CategoriaFragment;
import com.example.proyecto.principal.NavbarSide.ui.perfil.AgregarReceta.AgregarRecetaActivity;
import com.example.proyecto.principal.NavbarSide.ui.perfil.Editar.EditarPerfilActivity;
import com.example.proyecto.principal.NavbarSide.ui.perfil.Favoritos.Perfil_FavoritosFragment;
import com.example.proyecto.principal.NavbarSide.ui.perfil.Principal.Perfil_PrincipalFragment;

public class PerfilFragment extends Fragment   {
    private Button PbtnEditarPerfil,PbtnAgregarReceta;
    private ImageButton PbtnPrincipal,PbtnFavorito;
    private ImageButton selectedButton = null;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        PbtnEditarPerfil = view.findViewById(R.id.PbtnEditarPerfil);
        PbtnPrincipal = view.findViewById(R.id.PbtnPrincipal);
        PbtnFavorito = view.findViewById(R.id.PbtnFavorito);
        PbtnAgregarReceta = view.findViewById(R.id.PbtnAgregarReceta);
        if (savedInstanceState == null) {
            PerfilcargarFragmentInterno(new Perfil_PrincipalFragment());
            // Establecer el estado inicial del primer botón como seleccionado
            selectedButton = PbtnPrincipal;
        }

        PbtnEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iEditarPerfil = new Intent(requireContext(), EditarPerfilActivity.class);
                startActivity(iEditarPerfil);
            }
        });
        PbtnPrincipal.setOnClickListener(v -> {
            PerfilcargarFragmentInterno(new Perfil_PrincipalFragment());
        });
        PbtnFavorito.setOnClickListener(v -> {
            PerfilcargarFragmentInterno(new Perfil_FavoritosFragment());
        });
        PbtnAgregarReceta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iAgregarReceta = new Intent(requireContext(), AgregarRecetaActivity.class);
                startActivity(iAgregarReceta);
            }
        });
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clean up bindings if you were using View Binding,
        // but your current code uses findViewById, so this might not be necessary
        // unless you add View Binding later.
    }


    private void PerfilcargarFragmentInterno(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.PerfilcontenedorInterno, fragment)
                .commit();
    }
}