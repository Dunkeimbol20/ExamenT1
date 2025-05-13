package com.example.proyecto.principal.NavbarSide.ui.perfil;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.proyecto.R;
import com.example.proyecto.databinding.FragmentPerfilBinding;
import com.example.proyecto.principal.NavbarSide.ui.perfil.Editar.EditarPerfilActivity;

public class PerfilFragment extends Fragment   {
    Button PbtnEditarPerfil;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        PbtnEditarPerfil = view.findViewById(R.id.PbtnEditarPerfil);
        PbtnEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iEditarPerfil = new Intent(requireContext(), EditarPerfilActivity.class);
                startActivity(iEditarPerfil);
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
}