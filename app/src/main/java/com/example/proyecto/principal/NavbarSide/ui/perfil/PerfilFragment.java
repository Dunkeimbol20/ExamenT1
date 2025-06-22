package com.example.proyecto.principal.NavbarSide.ui.perfil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.content.Context;
import androidx.fragment.app.Fragment;

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
            selectedButton = PbtnPrincipal;
        }

        PbtnEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameDelUsuarioActual = obtenerUsernameDelUsuarioLogueado(); // Debes implementar este método o usar tu lógica

                if (usernameDelUsuarioActual != null && !usernameDelUsuarioActual.isEmpty()) {
                    Intent intent = new Intent(getActivity(), EditarPerfilActivity.class);

                    intent.putExtra("username", usernameDelUsuarioActual);

                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Error: No se pudo obtener el usuario actual.", Toast.LENGTH_SHORT).show();
                }
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

    }


    private void PerfilcargarFragmentInterno(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.PerfilcontenedorInterno, fragment)
                .commit();
    }
    private String obtenerUsernameDelUsuarioLogueado() {
        SharedPreferences preferences = getActivity().getSharedPreferences("MiAppPrefs", Context.MODE_PRIVATE);
        String username = preferences.getString("logged_in_username", null);
        return username;


    }
}