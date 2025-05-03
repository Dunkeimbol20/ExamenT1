package com.example.proyecto.principal.NavbarSide.ui.config;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyecto.R;
import com.example.proyecto.databinding.FragmentConfigBinding;
import com.example.proyecto.principal.NavbarSide.ui.config.ChangedUser.ChangedUserActivity;

public class ConfigFragment extends Fragment implements View.OnClickListener {
    ImageButton botonCambiarUsuario;
    private FragmentConfigBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ConfigViewModel homeViewModel =
                new ViewModelProvider(this).get(ConfigViewModel.class);

        View vista = inflater.inflate(R.layout.fragment_config, container, false);
        botonCambiarUsuario = vista.findViewById(R.id.btnCambiarUsuario);
        botonCambiarUsuario.setOnClickListener(this);

        //final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return vista;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnCambiarUsuario)
            cambiarUsuario();
    }

    private void cambiarUsuario() {
        Intent iPrincipal = new Intent(getContext(), ChangedUserActivity.class);
        startActivity(iPrincipal);
    }
}